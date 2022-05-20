package co.com.viveres.susy.microservicesupplier.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.viveres.susy.microservicecommons.dto.ProductDto;
import co.com.viveres.susy.microservicecommons.entity.MessageEntity;
import co.com.viveres.susy.microservicecommons.exception.GenericException;
import co.com.viveres.susy.microservicecommons.repository.IMessageRepository;
import co.com.viveres.susy.microservicesupplier.client.IProductRestClient;
import co.com.viveres.susy.microservicesupplier.dto.SupplierDto;
import co.com.viveres.susy.microservicesupplier.entity.ProductSupplierEntity;
import co.com.viveres.susy.microservicesupplier.entity.SupplierEntity;
import co.com.viveres.susy.microservicesupplier.repository.IProductSupplierRepository;
import co.com.viveres.susy.microservicesupplier.repository.ISupplierRepository;
import co.com.viveres.susy.microservicesupplier.util.ResponseMessages;

@Service
public class SupplierServiceImpl implements ISupplierService {

	@Autowired
	private ISupplierRepository supplierRepository;
	@Autowired
	private IProductSupplierRepository productSupplierRepository;
	@Autowired
	private IProductRestClient productClientRest;
	@Autowired
	private IMessageRepository messageRepository;

	@Override
	public SupplierDto create(SupplierDto request) {
		this.validateProviderAlreadyExist(request);
		SupplierEntity modelIn = this.mapInSupplierDtoToEntity(request);
		return this.mapOutSupplierEntityToDto(this.persist(modelIn));
	}

	private SupplierEntity persist(SupplierEntity modelIn) {
		return this.supplierRepository.save(modelIn);
	}

	private void validateProviderAlreadyExist(SupplierDto reques) {
		Optional<SupplierEntity> model = this.supplierRepository
				.findByIdentificationNumber(reques.getIdentificationNumber());
		if (model.isPresent()) {
			throw this.throwGenericException(ResponseMessages.SUPPLIER_ALREADY_EXISTS,
					model.get().getIdentificationNumber());
		}
	}

	private SupplierEntity mapInSupplierDtoToEntity(SupplierDto request) {
		SupplierEntity model = new SupplierEntity();
		model.setBusinessName(request.getName());
		model.setIdentificationNumber(request.getIdentificationNumber());
		model.setPhone(request.getPhone());
		return model;
	}

	private SupplierDto mapOutSupplierEntityToDto(SupplierEntity supplierEntity) {
		SupplierDto supplierDto = new SupplierDto();
		supplierDto.setId(supplierEntity.getId());
		supplierDto.setIdentificationNumber(supplierEntity.getIdentificationNumber());
		supplierDto.setName(supplierEntity.getBusinessName());
		supplierDto.setPhone(supplierEntity.getPhone());
		supplierDto.setProducts(new ArrayList<>());
		
		if (supplierEntity.getProductSupplierList() != null) {
			supplierEntity.getProductSupplierList().forEach(productSupplierModel -> {
				ResponseEntity<ProductDto> responseProductClient = this.productClientRest
						.findById(productSupplierModel.getProductId());
				ProductDto productDto = responseProductClient.getBody();
				supplierDto.getProducts().add(productDto);
			});
		}
		
		return supplierDto;
	}

	@Override
	public List<SupplierDto> findAll() {
		List<SupplierEntity> models = this.supplierRepository.findAll();
		return this.mapOutLIstSupplierEntityToDto(models);
	}

	private List<SupplierDto> mapOutLIstSupplierEntityToDto(List<SupplierEntity> models) {
		List<SupplierDto> dtos = new ArrayList<>();
		models.forEach(model -> {
			SupplierDto dto = this.mapOutSupplierEntityToDto(model);
			dtos.add(dto);
		});
		return dtos;
	}

	@Override
	public SupplierDto findById(Long id) {
		SupplierEntity model = this.findSupplierById(id);
		return this.mapOutSupplierEntityToDto(model);
	}

	@Override
	public void update(Long id, SupplierDto request) {
		SupplierEntity model = this.findSupplierById(id);

		model.setId(id);
		model.setIdentificationNumber(request.getIdentificationNumber());
		model.setBusinessName(request.getName());
		model.setPhone(request.getPhone());
		this.supplierRepository.save(model);

	}

	@Override
	public void delete(Long id) {
		SupplierEntity supplierModel = this.findSupplierById(id);
		this.productSupplierRepository.deleteAll(supplierModel.getProductSupplierList());
		this.supplierRepository.delete(supplierModel);
	}
	
	@Override
	public void associateProductToSupplier(Long supplierId, ProductDto product) {
		this.productClientRest.findById(product.getId());
		
		SupplierEntity supplierEntity = this.findSupplierById(supplierId);
		
		Optional<ProductSupplierEntity> productSupplierEntity = this.productSupplierRepository
				.findByProductIdAndSupplier(product.getId(), supplierEntity);
		
		if (!productSupplierEntity.isPresent()) {
			ProductSupplierEntity newProductSupplierEntity = new ProductSupplierEntity();
			newProductSupplierEntity.setProductId(product.getId());
			
			supplierEntity.setId(supplierId);
			supplierEntity.addProduct(newProductSupplierEntity);
			this.persist(supplierEntity);
		}
	}
	
	@Override
	public void disassociateProductToSupplier(Long supplierId, Long productId) {
		this.productClientRest.findById(productId);
		SupplierEntity supplierEntity = this.findSupplierById(supplierId);
		Optional<ProductSupplierEntity> productSupplierEntity = this.productSupplierRepository
				.findByProductIdAndSupplier(productId, supplierEntity);
		if (productSupplierEntity.isPresent()) {
			supplierEntity.getProductSupplierList().remove(productSupplierEntity.get());
		}
		this.persist(supplierEntity);
	}
	
	private SupplierEntity findSupplierById(Long id) {
		return this.supplierRepository
			.findById(id).orElseThrow(
			() -> this.throwGenericException(
					ResponseMessages.SUPPLIER_DOES_NOT_EXIST, String.valueOf(id)));
	}	
	
	private GenericException throwGenericException(String responseMessage, String value) {		
		MessageEntity message = this.messageRepository.findById(responseMessage)
				.orElseThrow(NoSuchElementException::new);
		message.setDescripction(String.format(message.getDescripction(), value));		
		return new GenericException(message);
	}
	
	/*private void removeProductsToSupplier(SupplierEntity model, List<Long> idsRemovedProducts) {
		idsRemovedProducts.forEach(idProduct -> {
			Optional<ProductSupplierEntity> respuesta = this.productSupplierRepository
					.findByProductIdAndSupplier(idProduct, model);
			if (respuesta.isPresent()) {
				model.getProductSupplierList().remove(respuesta.get());
			}
		});
	}
	
	private void addNewProductsToSupplier(SupplierEntity model, List<Long> idsNewProducts) {
		idsNewProducts.forEach(idProduct -> {
			this.productClientRest.getById(idProduct);

			ProductSupplierEntity modelProductSupplier = new ProductSupplierEntity();
			modelProductSupplier.setProductId(idProduct);
			model.addProduct(modelProductSupplier);
		});
	}*/

}
