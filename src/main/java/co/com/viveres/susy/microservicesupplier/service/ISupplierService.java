package co.com.viveres.susy.microservicesupplier.service;

import java.util.List;

import co.com.viveres.susy.microservicecommons.dto.ProductDto;
import co.com.viveres.susy.microservicesupplier.dto.SupplierDto;

public interface ISupplierService {

	SupplierDto create(SupplierDto request);

	List<SupplierDto> findAll();

	SupplierDto findById(Long id);

	void update(Long id, SupplierDto request);

	void delete(Long id);

	void associateProductToSupplier(Long supplierId, ProductDto product);

	void disassociateProductToSupplier(Long supplierId, Long product);

}
