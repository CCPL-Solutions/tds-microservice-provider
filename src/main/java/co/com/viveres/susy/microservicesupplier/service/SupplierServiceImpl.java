package co.com.viveres.susy.microservicesupplier.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.com.viveres.susy.microservicecommons.entity.MessageEntity;
import co.com.viveres.susy.microservicecommons.exceptions.GenericException;
import co.com.viveres.susy.microservicecommons.repository.IMessageRepository;
import co.com.viveres.susy.microservicesupplier.client.IProductRestClient;
import co.com.viveres.susy.microservicesupplier.dto.ContentDto;
import co.com.viveres.susy.microservicesupplier.dto.ProductDto;
import co.com.viveres.susy.microservicesupplier.dto.ResponseProductClientProductDto;
import co.com.viveres.susy.microservicesupplier.dto.SupplierInputDto;
import co.com.viveres.susy.microservicesupplier.dto.SupplierOutputDto;
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
	public SupplierOutputDto create(SupplierInputDto request) {
		this.validateProviderAlreadyExist(request);

		SupplierEntity modelIn = this.mapInSupplierDtoToEntity(request);

		SupplierEntity modelOut = this.supplierRepository.save(modelIn);
		return this.mapOutSupplierEntityToDto(modelOut);
	}

	private void validateProviderAlreadyExist(SupplierInputDto reques) {

		Optional<SupplierEntity> model = this.supplierRepository
				.findByIdentificationNumber(reques.getIdentificationNumber());

		if (model.isPresent()) {
			throw this.throwGenericException(ResponseMessages.SUPPLIER_ALREADY_EXISTS,
					model.get().getIdentificationNumber());
		}
	}

	private SupplierEntity mapInSupplierDtoToEntity(SupplierInputDto request) {

		SupplierEntity model = new SupplierEntity();

		model.setBusinessName(request.getName());
		model.setIdentificationNumber(request.getIdentificationNumber());
		model.setPhone(request.getPhone());
		
		List<Long>idsNewProducts = request.getProducts().stream().map(ProductDto::getId)
				.collect(Collectors.toList());
		this.addNewProductsToSupplier(model, idsNewProducts);

		return model;
	}
	
	private void addNewProductsToSupplier(SupplierEntity model, List<Long> idsNewProducts) {
		idsNewProducts.forEach(idProduct -> {
			this.productClientRest.getById(idProduct);

			ProductSupplierEntity modelProductSupplier = new ProductSupplierEntity();
			modelProductSupplier.setProductId(idProduct);
			model.addProduct(modelProductSupplier);
		});
	}

	private SupplierOutputDto mapOutSupplierEntityToDto(SupplierEntity model) {

		SupplierOutputDto dto = new SupplierOutputDto();
		dto.setId(model.getId());
		dto.setIdentificationNumber(model.getIdentificationNumber());
		dto.setName(model.getBusinessName());
		dto.setPhone(model.getPhone());
		dto.setProducts(new ArrayList<>());

		model.getProductSupplierList().forEach(productSupplierModel -> {

			ResponseEntity<ResponseProductClientProductDto> responseProductClient = this.productClientRest
					.getById(productSupplierModel.getProductId());
			ResponseProductClientProductDto responseBody = responseProductClient.getBody();

			ProductDto productDto = new ProductDto();
			productDto.setId(responseBody.getId());
			productDto.setName(responseBody.getName());
			productDto.setBrand(responseBody.getBrand());
			productDto.setContent(new ContentDto());
			productDto.getContent().setMeasure(responseBody.getContent().getMeasure());
			productDto.getContent().setValue(responseBody.getContent().getValue());

			dto.getProducts().add(productDto);
		});

		return dto;
	}

	@Override
	public List<SupplierOutputDto> getAll() {

		List<SupplierEntity> models = this.supplierRepository.findAll();

		return this.mapOutLIstSupplierEntityToDto(models);
	}

	private List<SupplierOutputDto> mapOutLIstSupplierEntityToDto(List<SupplierEntity> models) {

		List<SupplierOutputDto> dtos = new ArrayList<>();
		models.forEach(model -> {
			SupplierOutputDto dto = this.mapOutSupplierEntityToDto(model);
			dtos.add(dto);
		});

		return dtos;
	}

	@Override
	public SupplierOutputDto getById(Long id) {
		SupplierEntity model = this.supplierRepository.findById(id).orElseThrow(
				() -> this.throwGenericException(ResponseMessages.SUPPLIER_DOES_NOT_EXIST, String.valueOf(id)));
		
		return this.mapOutSupplierEntityToDto(model);
	}

	@Override
	public void update(Long id, SupplierInputDto request) {

		SupplierEntity model = this.supplierRepository.findById(id).orElseThrow(
				() -> this.throwGenericException(ResponseMessages.SUPPLIER_DOES_NOT_EXIST, String.valueOf(id)));

		List<Long> idsExistingProductInDatabase = model.getProductSupplierList().stream().map(ProductSupplierEntity::getProductId).collect(Collectors.toList());
		List<Long> idsProductInRequest = request.getProducts().stream().map(ProductDto::getId).collect(Collectors.toList());

		List<Long> idsNewProducts = (List<Long>) CollectionUtils.removeAll(idsProductInRequest, idsExistingProductInDatabase);
		List<Long> idsRemovedProducts = (List<Long>) CollectionUtils.removeAll(idsExistingProductInDatabase, idsProductInRequest);

		model.setId(id);
		model.setIdentificationNumber(request.getIdentificationNumber());
		model.setBusinessName(request.getName());
		model.setPhone(request.getPhone());

		this.addNewProductsToSupplier(model, idsNewProducts);
		this.removeProductsToSupplier(model, idsRemovedProducts);

		this.supplierRepository.save(model);

	}
	
	private void removeProductsToSupplier(SupplierEntity model, List<Long> idsRemovedProducts) {
		idsRemovedProducts.forEach(idProduct -> {
			Optional<ProductSupplierEntity> respuesta = this.productSupplierRepository
					.findByProductIdAndSupplier(idProduct, model);
			if (respuesta.isPresent()) {
				model.getProductSupplierList().remove(respuesta.get());
			}
		});
	}

	@Override
	public void delete(Long id) {

		SupplierEntity supplierModel = this.supplierRepository.findById(id).orElseThrow(
				() -> this.throwGenericException(ResponseMessages.SUPPLIER_DOES_NOT_EXIST, String.valueOf(id)));

		this.productSupplierRepository.deleteAll(supplierModel.getProductSupplierList());
		this.supplierRepository.delete(supplierModel);

	}
	
private GenericException throwGenericException(String responseMessage, String value) {
		
		MessageEntity message = this.messageRepository.findById(responseMessage)
				.orElseThrow(NoSuchElementException::new);
		message.setDescripction(String.format(message.getDescripction(), value));
		
		
		return new GenericException(message);
	}

}
