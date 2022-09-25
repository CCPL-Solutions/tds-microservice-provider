package co.com.viveres.susy.microservicesupplier.client;

import co.com.viveres.susy.microservicecommons.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-product", path = "/v1/products")
public interface IProductRestClient {

    @GetMapping("/{product-id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("product-id") Long productId);

}
