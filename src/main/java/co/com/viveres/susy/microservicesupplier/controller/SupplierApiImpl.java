package co.com.viveres.susy.microservicesupplier.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import co.com.viveres.susy.microservicecommons.dto.ProductDto;
import co.com.viveres.susy.microservicesupplier.api.ISupplierApi;
import co.com.viveres.susy.microservicesupplier.dto.SupplierDto;
import co.com.viveres.susy.microservicesupplier.service.ISupplierService;

@RestController
@RequestMapping("/v1/suppliers")
public class SupplierApiImpl implements ISupplierApi {

	@Autowired
	private ISupplierService service;

	@Override
	public ResponseEntity<SupplierDto> create(SupplierDto request) {
		SupplierDto response = service.create(request);
		return this.buildCreatResponse(response);
	}

	@Override
	public ResponseEntity<Page<SupplierDto>> getAll(int page, int size, String sort) {
		Page<SupplierDto> response = service.findAll(page, size, sort);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<SupplierDto> getById(Long id) {
		SupplierDto response = service.findById(id);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Void> update(Long id, SupplierDto request) {
		service.update(id, request);
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Void> delete(Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}
	
	@Override
	public ResponseEntity<Void> associateProductToSupplier(Long supplierId, ProductDto product) {
		this.service.associateProductToSupplier(supplierId, product);
		return ResponseEntity.ok().build();
	}
	
	@Override
	public ResponseEntity<Void> disassociateProductToSupplier(Long supplierId, Long productId) {
		this.service.disassociateProductToSupplier(supplierId, productId);
		return ResponseEntity.ok().build();
	}

	private ResponseEntity<SupplierDto> buildCreatResponse(SupplierDto response) {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{supplier-id}")
				.buildAndExpand(response.getId()).toUri();
		
		return ResponseEntity.created(uri).body(response);
	}

}
