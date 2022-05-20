package co.com.viveres.susy.microservicesupplier.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import co.com.viveres.susy.microservicecommons.dto.ProductDto;
import lombok.Data;

@Data
public class SupplierDto {

	private Long id;
	
	@NotBlank
	private String name;

	@NotBlank
	private String phone;

	@NotBlank
	private String identificationNumber;
	
	private List<ProductDto> products;

}
