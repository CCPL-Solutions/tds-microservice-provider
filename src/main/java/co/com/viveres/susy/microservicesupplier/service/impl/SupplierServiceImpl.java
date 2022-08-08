package co.com.viveres.susy.microservicesupplier.service.impl;

import co.com.viveres.susy.microservicecommons.dto.ProductDto;
import co.com.viveres.susy.microservicecommons.exception.BusinessException;
import co.com.viveres.susy.microservicecommons.exception.NotFoundException;
import co.com.viveres.susy.microservicesupplier.client.IProductRestClient;
import co.com.viveres.susy.microservicesupplier.dto.SupplierDto;
import co.com.viveres.susy.microservicesupplier.entity.ProductSupplierEntity;
import co.com.viveres.susy.microservicesupplier.entity.SupplierEntity;
import co.com.viveres.susy.microservicesupplier.repository.IProductSupplierRepository;
import co.com.viveres.susy.microservicesupplier.repository.ISupplierRepository;
import co.com.viveres.susy.microservicesupplier.service.ISupplierService;
import co.com.viveres.susy.microservicesupplier.service.mapper.IMapper;
import co.com.viveres.susy.microservicesupplier.util.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SupplierServiceImpl implements ISupplierService {

	@Autowired
	private ISupplierRepository supplierRepository;
	@Autowired
	private IProductSupplierRepository productSupplierRepository;
	@Autowired
	private IProductRestClient productClientRest;
	@Autowired
	private IMapper mapper;

	@Override
	public SupplierDto create(SupplierDto supplierDto) {
		this.validateProviderAlreadyExist(supplierDto);
		SupplierEntity supplierEntity = this.mapper.mapInSupplierDtoToEntity(supplierDto);
		SupplierDto supplierDtoOut = this.mapper.mapOutSupplierEntityToDto(this.persist(supplierEntity));
		this.setProductsToSuppliers(supplierDtoOut, supplierEntity);
		return supplierDtoOut;
	}

	private SupplierEntity persist(SupplierEntity modelIn) {
		return this.supplierRepository.save(modelIn);
	}

	private void validateProviderAlreadyExist(SupplierDto supplierDto) {
		Optional<SupplierEntity> model = this.supplierRepository
				.findByIdentificationNumber(supplierDto.getIdentificationNumber());
		if (model.isPresent()) {
			throw new BusinessException(ResponseMessages.SUPPLIER_ALREADY_EXISTS);
		}
	}

	private void setProductsToSuppliers(SupplierDto supplierDto,
										SupplierEntity supplierEntity) {
		if (supplierEntity.getProductSupplierList() != null) {
			supplierDto.setProducts(new ArrayList<>());
			supplierEntity.getProductSupplierList().forEach(productSupplierModel -> {
				ResponseEntity<ProductDto> responseProductClient = this.productClientRest
						.findById(productSupplierModel.getProductId());
				ProductDto productDto = responseProductClient.getBody();
				supplierDto.getProducts().add(productDto);
			});
		}
	}

	@Override
	public List<SupplierDto> findAll() {
		List<SupplierEntity> supplierEntityList = this.supplierRepository.findAll();
		return this.mapOutLIstSupplierEntityToDto(supplierEntityList);
	}

	private List<SupplierDto> mapOutLIstSupplierEntityToDto(List<SupplierEntity> supplierEntityList) {
		List<SupplierDto> supplierDtoList = new ArrayList<>();
		supplierEntityList.forEach(supplierEntity -> {
			SupplierDto dto = this.mapper.mapOutSupplierEntityToDto(supplierEntity);
			supplierDtoList.add(dto);
		});
		return supplierDtoList;
	}

	@Override
	public SupplierDto findById(Long supplierId) {
		SupplierEntity supplierEntity = this.findSupplierById(supplierId);
		return this.mapper.mapOutSupplierEntityToDto(supplierEntity);
	}

	@Override
	public void update(Long supplierId, SupplierDto supplierDto) {
		SupplierEntity supplierEntity = this.findSupplierById(supplierId);
		this.mapper.mapInSupplierDtoToEntityUpdate(supplierEntity, supplierDto);
		this.supplierRepository.save(supplierEntity);
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
		return this.supplierRepository.findById(id).orElseThrow(
				() -> new NotFoundException(ResponseMessages.SUPPLIER_DOES_NOT_EXIST));
	}

}
