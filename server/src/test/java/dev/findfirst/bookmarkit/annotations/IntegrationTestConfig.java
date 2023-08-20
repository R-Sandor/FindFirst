package dev.findfirst.bookmarkit.annotations;

import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.boot.test.context.SpringBootTest;

@EnabledIf(value = "#{environment.getActiveProfiles()[0] == 'integration'}")
@SpringBootTest
public @interface IntegrationTestConfig {}
