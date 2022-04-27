package co.com.viveres.susy.microservicesupplier.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import co.com.viveres.susy.microservicesupplier.dto.SupplierInputDto;
import co.com.viveres.susy.microservicesupplier.dto.SupplierOutputDto;
import co.com.viveres.susy.microservicesupplier.dto.validation.ICreateSupplierValidation;

public interface ISupplierApi {

    @PostMapping(
        path = "", 
        consumes = MediaType.APPLICATION_JSON_VALUE, 
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupplierOutputDto> create(
        @Validated(ICreateSupplierValidation.class) @RequestBody SupplierInputDto request);

    @GetMapping(
        path = "", 
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SupplierOutputDto>> getAll();

    @GetMapping(
        path = "/{supplier-id}", 
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupplierOutputDto> getById(@PathVariable("supplier-id") Long id);

    @PutMapping(
        path = "/{supplier-id}", 
        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update(@PathVariable("supplier-id") Long id, @RequestBody SupplierInputDto request);

    @DeleteMapping(path = "/{supplier-id}")
    public ResponseEntity<Void> delete(@PathVariable("supplier-id") Long id);

}
