package co.com.viveres.susy.microservicesupplier.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import co.com.viveres.susy.microservicesupplier.dto.validation.ICreateSupplierValidation;
import lombok.Data;

@Data
public class SupplierInputDto extends BaseSupplierDto {

    @Valid
    @NotNull(groups = ICreateSupplierValidation.class)
    private List<ProductDto> products;

}
