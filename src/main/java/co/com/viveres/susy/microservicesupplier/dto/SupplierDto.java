package co.com.viveres.susy.microservicesupplier.dto;

import co.com.viveres.susy.microservicecommons.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.List;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
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
