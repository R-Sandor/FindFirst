package dev.findfirst.security.conditions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.mock.env.MockEnvironment;

@ExtendWith(MockitoExtension.class)
class Oauth2ClientsTest {

  @Mock
  private ConditionContext context;

  private MockEnvironment mockEnvironment = new MockEnvironment();

  @BeforeEach
  void setUp() {
    when(context.getEnvironment()).thenReturn(mockEnvironment);
  }

  @Test
  void noRegisteredClientsInProperties() {
    assertFalse(new OAuthClientsCondition().matches(context, null));
  }

  @Test
  void conditionShouldReturnTrueWithOneClientRegistered() {
    this.mockEnvironment.setProperty("spring.security.oauth2.client.registration.github.client-id",
        "testvalues");
    this.mockEnvironment.setProperty(
        "spring.security.oauth2.client.registration.github.client-secret", "testvalues");
    assertTrue(new OAuthClientsCondition().matches(context, null));
  }

  @Test
  void conditionShouldReturnFalseMissingData() {
    this.mockEnvironment.setProperty("spring.security.oauth2.client.registration.github.client-id",
        "testvalues");
    this.mockEnvironment
        .setProperty("spring.security.oauth2.client.registration.github.client-secret", "");
    assertFalse(new OAuthClientsCondition().matches(context, null));
  }

  @Test
  void emptyRegistrationsShouldReturnFalse() {
    this.mockEnvironment.setProperty("spring.security.oauth2.client.registration.github.client-id",
        "");
    this.mockEnvironment
        .setProperty("spring.security.oauth2.client.registration.github.client-secret", "");
    assertFalse(new OAuthClientsCondition().matches(context, null));
  }
}
