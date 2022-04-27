package co.com.viveres.susy.microservicesupplier.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import co.com.viveres.susy.microservicesupplier.dto.validation.ICreateSupplierValidation;
import lombok.Data;

@Data
public class ProductDto implements Serializable{

    @NotNull(groups = ICreateSupplierValidation.class)
    private Long id;

    private String name;
    private String brand;
    private ContentDto content;
}
