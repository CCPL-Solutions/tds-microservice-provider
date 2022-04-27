package co.com.viveres.susy.microservicesupplier.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import co.com.viveres.susy.microservicesupplier.api.ISupplierApi;
import co.com.viveres.susy.microservicesupplier.dto.SupplierInputDto;
import co.com.viveres.susy.microservicesupplier.dto.SupplierOutputDto;
import co.com.viveres.susy.microservicesupplier.service.ISupplierService;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/supplier")
public class SupplierApiImpl implements ISupplierApi {

	@Autowired
	private ISupplierService service;

	@Override
	public ResponseEntity<SupplierOutputDto> create(SupplierInputDto request) {
		SupplierOutputDto response = service.create(request);
		return this.buildCreatResponse(response);
	}

	@Override
	public ResponseEntity<List<SupplierOutputDto>> getAll() {
		List<SupplierOutputDto> response = service.getAll();
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<SupplierOutputDto> getById(Long id) {
		SupplierOutputDto response = service.getById(id);
		return ResponseEntity.ok(response);
	}

	@Override
	public ResponseEntity<Void> update(Long id, SupplierInputDto request) {
		service.update(id, request);
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<Void> delete(Long id) {
		service.delete(id);
		return ResponseEntity.ok().build();
	}

	private ResponseEntity<SupplierOutputDto> buildCreatResponse(SupplierOutputDto response) {
		URI uri = ServletUriComponentsBuilder
				.fromCurrentRequest().path("/{supplier-id}")
				.buildAndExpand(response.getId()).toUri();
		
		return ResponseEntity.created(uri).body(response);
	}

}
