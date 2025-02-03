package dev.findfirst.core.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.typesense.api.Client;
import org.typesense.resources.Node;

@Configuration
@Slf4j
public class TypesenseConfiguration {

  @Value("${findfirst.typesense.api_key}")
  String typesSenseApiKey;

  @Value("${findfirst.typesense.host}")
  String host;

  @Bean
  public Client typesenseClient() {
    List<Node> nodes = new ArrayList<>();
    nodes.add(new Node("http", host, "8108"));
    org.typesense.api.Configuration configuration =
        new org.typesense.api.Configuration(nodes, Duration.ofSeconds(9), typesSenseApiKey);
    return new Client(configuration);
  }

}
