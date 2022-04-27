package co.com.viveres.susy.microservicesupplier.dto;

import lombok.Data;

@Data
public class ResponseProductClientProductDto{

    private Long id;
    private ResponseProductClientContentDto content;
    private String name;
    private String brand;
    private Double price;
    private Integer currentNumItems;
    private Integer minimunStock;

}
