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

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Binder binder = Binder.get(context.getEnvironment());
    Map<String, String> properties = binder.bind("spring.security.oauth2.client.registration",
        Bindable.mapOf(String.class, String.class)).orElse(Collections.emptyMap());

    Map<String, ClientPair> clients = new HashMap<>();
    properties.forEach((prop, val) -> {
      if (prop.contains("client-secret")) {
        String c = prop.substring(0, prop.lastIndexOf("."));
        var p = clients.get(c);
        System.out.println(c);
        if (p != null) {
          clients.put(c, new ClientPair(p.clientId(), val));
        } else {
          if (val != null && !val.isBlank()) {
            clients.put(c, new ClientPair(null, val));
          }
        }
      } else if (prop.contains("client-id")) {
        if (val != null && !val.isBlank()) {
          String c = prop.substring(0, prop.lastIndexOf("."));
          var p = clients.get(c);
          System.out.println(c);
          if (p != null) {
            clients.put(c, new ClientPair(val, p.clientSecret()));
          } else {
            if (val != null && !val.isBlank()) {
              clients.put(c, new ClientPair(val, null));
            }
          }
        }
      }
    });
    System.out.println(clients);
    var registrations = clients.values().stream().peek(System.out::println).filter(cp -> {
      System.out.println(!cp.clientId().isEmpty() && !cp.clientSecret().isEmpty());
      return (!cp.clientId().isBlank() && !cp.clientSecret().isBlank());
    }).toList();
    System.out.println(registrations);
    return !properties.isEmpty() && !registrations.isEmpty();
  }

  record ClientPair(String clientId, String clientSecret) {};

}
