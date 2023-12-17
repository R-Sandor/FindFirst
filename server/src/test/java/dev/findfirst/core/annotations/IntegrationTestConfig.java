package dev.findfirst.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@EnabledIf(
    value = "#{{'test', 'prod'}.contains(environment.getActiveProfiles()[0])}",
    loadContext = true)
@SpringBootTest
public @interface IntegrationTestConfig {}
