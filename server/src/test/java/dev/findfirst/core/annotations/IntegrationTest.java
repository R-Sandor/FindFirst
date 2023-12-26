package dev.findfirst.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'integration'}")
public @interface IntegrationTest {}
