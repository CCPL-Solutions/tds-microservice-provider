package co.com.viveres.susy.microservicesupplier.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ContentDto implements Serializable{

    private String measure;
    private Integer value;

}
