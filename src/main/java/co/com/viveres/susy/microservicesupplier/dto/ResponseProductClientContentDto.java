package co.com.viveres.susy.microservicesupplier.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponseProductClientContentDto implements Serializable{

    private Long id;
    private String measure;
    private Integer value;

}
