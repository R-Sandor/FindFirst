package dev.findfirst.security.conditions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OAuthClientsCondition implements Condition {

  Map<String, ClientPair> clients = new HashMap<>();

  @SuppressWarnings("null")
  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Binder binder = Binder.get(context.getEnvironment());

    Map<String, String> properties = binder.bind("spring.security.oauth2.client.registration",
        Bindable.mapOf(String.class, String.class)).orElse(Collections.emptyMap());

    properties.forEach((prop, val) -> {
      String clientName = prop.substring(0, prop.lastIndexOf("."));
      if (prop.contains("client-id")) {
        setClientIdToPair(clientName, val);
      } else if (prop.contains("client-secret")) {
        setSecretToPair(clientName, val);
      }
    });

    var registrations = clients.values().stream()
        .filter(cp -> !cp.clientId().isBlank() && !cp.clientSecret().isBlank()).toList();
    return !properties.isEmpty() && !registrations.isEmpty();

  }

  private void setClientIdToPair(String clientName, String propVal) {
    if (propVal == null || propVal.isBlank()) {
      return;
    }

    var p = clients.get(clientName);
    if (p != null) {
      clients.put(clientName, new ClientPair(propVal, p.clientSecret()));
    } else {
      clients.put(clientName, new ClientPair(propVal, ""));
    }
  }

  private void setSecretToPair(String clientName, String propVal) {
    if (propVal == null || propVal.isBlank()) {
      return;
    }
    var p = clients.get(clientName);
    if (p != null) {
      clients.put(clientName, new ClientPair(p.clientId(), propVal));
    } else {
      clients.put(clientName, new ClientPair("", propVal));
    }
  }

  record ClientPair(String clientId, String clientSecret) {}

}
