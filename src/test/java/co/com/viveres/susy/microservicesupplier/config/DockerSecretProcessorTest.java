package co.com.viveres.susy.microservicesupplier.config;

import co.com.viveres.susy.microservicesupplier.controller.DockerSecretProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

class DockerSecretProcessorTest {

  @Test
  void postProcessEnvironment() {
    DockerSecretProcessor dockerSecretProcessor = new DockerSecretProcessor();
    Properties properties = new Properties();
    properties.setProperty("docker-secret.bind-path", "src/test/resources/SecretsTest");
    ConfigurableEnvironment environment = getEnvironment(dockerSecretProcessor, properties);
    Assertions.assertEquals("DB_NAME_TEST", environment.getProperty("tds-config-db-name"));
  }

  private ConfigurableEnvironment getEnvironment(EnvironmentPostProcessor processor, Properties properties) {
    SpringApplication springApplication = new SpringApplicationBuilder()
        .sources(DockerSecretProcessor.class)
        .web(WebApplicationType.NONE).build();
    ConfigurableApplicationContext context = springApplication.run();
    ConfigurableEnvironment environment = context.getEnvironment();
    environment.getPropertySources().addFirst(new PropertiesPropertySource("test", properties));
    processor.postProcessEnvironment(environment, springApplication);
    context.close();
    return environment;
  }
}