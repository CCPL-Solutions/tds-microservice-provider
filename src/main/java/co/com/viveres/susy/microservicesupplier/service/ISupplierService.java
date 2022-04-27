package co.com.viveres.susy.microservicesupplier.service;

import java.util.List;

import co.com.viveres.susy.microservicesupplier.dto.SupplierInputDto;
import co.com.viveres.susy.microservicesupplier.dto.SupplierOutputDto;

public interface ISupplierService {

    public SupplierOutputDto create(SupplierInputDto request);

    public List<SupplierOutputDto> getAll();

    public SupplierOutputDto getById(Long id);

    public void update(Long id, SupplierInputDto request);

    public void delete(Long id);

}
