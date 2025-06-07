package dev.findfirst.security.conditions;

import java.util.Collections;
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
    return !properties.isEmpty();
  }

}
