package dev.findfirst.security.conditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OAuthClientsCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Binder binder = Binder.get(context.getEnvironment());
    Map<String, String> properties = binder
        .bind("spring.security.oauth2.client.registration", Bindable.mapOf(String.class, String.class))
        .orElse(Collections.emptyMap());

    Map<String, ClientPair> client = new HashMap<>();
    properties.forEach((prop, val) -> {
      if (prop.contains("client-secret")) {
        String c = prop.substring(0, prop.lastIndexOf("."));
        var p = client.get(c);
        if (p != null) {
          client.put(prop, new ClientPair(p.clientId(), val));
        } else {
          if (val != null && !val.isBlank()) {
            client.put(prop, new ClientPair(null, val));
          }
        }
      } else if (prop.contains("client-id")) {
        if (val != null && !val.isBlank()) {
          String c = prop.substring(0, prop.lastIndexOf("."));
          var p = client.get(c);
          if (p != null) {
            client.put(prop, new ClientPair(val, p.clientSecret()));
          } else {
            if (val != null && !val.isBlank()) {
              client.put(prop, new ClientPair(val, null));
            }
          }
        }
      }
    });

    var registrations = client.values().stream().filter(cp -> cp.clientId() == null || cp.clientSecret() == null)
        .toList();
    return !properties.isEmpty() && !registrations.isEmpty();
  }

  record ClientPair(String clientId, String clientSecret) {
  };

}
