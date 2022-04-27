package co.com.viveres.susy.microservicesupplier.dto;

import java.util.List;

import lombok.Data;

@Data
public class SupplierOutputDto extends BaseSupplierDto {

    private Long id;
    private List<ProductDto> products;

}
