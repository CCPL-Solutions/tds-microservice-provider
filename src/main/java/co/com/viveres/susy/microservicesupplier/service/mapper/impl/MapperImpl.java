package co.com.viveres.susy.microservicesupplier.service.mapper.impl;

import co.com.viveres.susy.microservicesupplier.dto.SupplierDto;
import co.com.viveres.susy.microservicesupplier.entity.SupplierEntity;
import co.com.viveres.susy.microservicesupplier.service.mapper.IMapper;
import org.springframework.stereotype.Service;

@Service
public class MapperImpl implements IMapper {

    @Override
    public SupplierEntity mapInSupplierDtoToEntity(SupplierDto request) {
        return SupplierEntity.builder()
                .businessName(request.getName())
                .identificationNumber(request.getIdentificationNumber())
                .phone(request.getPhone())
                .build();
    }

    @Override
    public SupplierDto mapOutSupplierEntityToDto(SupplierEntity supplierEntity) {
        return SupplierDto.builder()
                .id(supplierEntity.getId())
                .identificationNumber(supplierEntity.getIdentificationNumber())
                .name(supplierEntity.getBusinessName())
                .phone(supplierEntity.getPhone())
                .build();
    }

    @Override
    public SupplierDto mapInSupplierDtoToEntityUpdate(SupplierEntity supplierEntity, SupplierDto supplierDto) {
        return SupplierDto.builder()
                .id(supplierEntity.getId())
                .identificationNumber(supplierDto.getIdentificationNumber())
                .name(supplierDto.getName())
                .phone(supplierDto.getPhone())
                .build();
    }

}
