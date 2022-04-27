package co.com.viveres.susy.microservicesupplier.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import co.com.viveres.susy.microservicesupplier.dto.validation.ICreateSupplierValidation;
import lombok.Data;

@Data
public class BaseSupplierDto implements Serializable {

    @NotNull(groups = ICreateSupplierValidation.class)
    private String name;

    @NotNull(groups = ICreateSupplierValidation.class)
    private String phone;

    @NotNull(groups = ICreateSupplierValidation.class)
    private String identificationNumber;

}
