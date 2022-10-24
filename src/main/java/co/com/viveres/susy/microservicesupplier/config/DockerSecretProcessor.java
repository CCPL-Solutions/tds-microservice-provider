package co.com.viveres.susy.microservicesupplier.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DockerSecretProcessor implements EnvironmentPostProcessor {

  private static final Logger LOGGER = LoggerFactory.getLogger(DockerSecretProcessor.class);

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

    String bindPathPpty = environment.getProperty("docker-secret.bind-path");

    System.out.println("value of \"docker-secret.bind-path\" property:" + bindPathPpty);

    if (bindPathPpty != null) {
      Path bindPath = Paths.get(bindPathPpty);
      if (Files.isDirectory(bindPath)) {
        Map<String, Object> dockerSecrets;
        try (Stream<Path> pathStream = Files.list(bindPath)) {
          dockerSecrets = pathStream.collect(
              Collectors.toMap(
                  path -> {
                    File secretFile = path.toFile();
                    return "tds-config-" + secretFile.getName();
                  },
                  path -> {
                    File secretFile = path.toFile();
                    try {
                      byte[] content = FileCopyUtils.copyToByteArray(secretFile);
                      return new String(content);
                    } catch (IOException e) {
                      throw new RuntimeException(e);
                    }
                  }
              ));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }

        dockerSecrets
            .entrySet()
            .forEach(entry -> {
              System.out.println(entry.getKey() + "=\"" + entry.getValue() + "\"");
            });

        MapPropertySource pptySource = new MapPropertySource("docker-secrets", dockerSecrets);

        environment.getPropertySources().addLast(pptySource);

      }
    }

  }
}
