package co.com.viveres.susy.microservicesupplier.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import co.com.viveres.susy.microservicecommons.dto.ProductDto;

@FeignClient(
	name = "microservice-product", 
	path = "/v1/products", 
	configuration = ClientConfiguration.class)
public interface IProductRestClient {

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("product-id") Long id);

}
