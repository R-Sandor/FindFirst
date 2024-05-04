package dev.findfirst.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@TestPropertySource(
    locations = "classpath:application-test.yml"
    // properties = {"findfirst.app.db=postgres:16.2-alpine3.19"}
    )
@EnabledIf(expression = "#{environment['spring.profiles.active'] == 'integration'}")
public @interface IntegrationTest {}
