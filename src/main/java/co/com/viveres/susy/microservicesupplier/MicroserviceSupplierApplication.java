package co.com.viveres.susy.microservicesupplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableCircuitBreaker
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@ComponentScan(basePackages = {"co.com.viveres.susy"})
public class MicroserviceSupplierApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MicroserviceSupplierApplication.class, args);
	}

}
