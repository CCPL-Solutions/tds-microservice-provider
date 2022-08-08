package co.com.viveres.susy.microservicesupplier.service.mapper;

import co.com.viveres.susy.microservicesupplier.dto.SupplierDto;
import co.com.viveres.susy.microservicesupplier.entity.SupplierEntity;

public interface IMapper {

    SupplierEntity mapInSupplierDtoToEntity(SupplierDto request);

    SupplierDto mapOutSupplierEntityToDto(SupplierEntity supplierEntity);

    SupplierDto mapInSupplierDtoToEntityUpdate(SupplierEntity supplierEntity, SupplierDto supplierDt);

}
