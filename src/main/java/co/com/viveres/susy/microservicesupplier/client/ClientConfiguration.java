package co.com.viveres.susy.microservicesupplier.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class ClientConfiguration {
	
	@Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

}
