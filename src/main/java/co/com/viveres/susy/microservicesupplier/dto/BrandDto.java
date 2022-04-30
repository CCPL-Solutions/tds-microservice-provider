package co.com.viveres.susy.microservicesupplier.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BrandDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;

}
