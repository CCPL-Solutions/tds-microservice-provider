package co.com.viveres.susy.microservicesupplier.service;

import java.util.List;

import co.com.viveres.susy.microservicecommons.dto.ProductDto;
import co.com.viveres.susy.microservicesupplier.dto.SupplierDto;
import org.springframework.data.domain.Page;

public interface ISupplierService {

	SupplierDto create(SupplierDto request);

	Page<SupplierDto> findAll(int page, int size, String sort);

	SupplierDto findById(Long id);

	void update(Long id, SupplierDto request);

	void delete(Long id);

	void associateProductToSupplier(Long supplierId, ProductDto product);

	void disassociateProductToSupplier(Long supplierId, Long product);

}
