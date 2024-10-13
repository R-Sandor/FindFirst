package dev.findfirst.core.controller;

import java.util.Optional;

import dev.findfirst.security.userAuth.models.TokenRefreshResponse;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class HttpUtility {

  public static HttpEntity<?> getHttpEntity(TestRestTemplate restTemplate) {
    HttpHeaders headers = new HttpHeaders();
    // test user
    headers.setBasicAuth("jsmith", "test");
    HttpEntity<String> entity = new HttpEntity<>(headers);
    var signResp = restTemplate.postForEntity("/user/signin", entity, TokenRefreshResponse.class);

    // Get the cookie from signin.
    var cookieOpt = Optional.ofNullable(signResp.getHeaders().get("Set-Cookie"));
    var cookie = cookieOpt.orElseThrow();

    // Add the cookie to next request.
    headers = new HttpHeaders();
    headers.add("Cookie", cookie.get(0));
    return new HttpEntity<>(headers);
  }

  public static <T> HttpEntity<?> getHttpEntity(TestRestTemplate restTemplate, T body) {
    HttpHeaders headers = new HttpHeaders();
    // test user
    headers.setBasicAuth("jsmith", "test");
    HttpEntity<String> entity = new HttpEntity<>(headers);
    var signResp = restTemplate.postForEntity("/user/signin", entity, TokenRefreshResponse.class);

    // Get the cookie from signin.
    var cookieOpt = Optional.ofNullable(signResp.getHeaders().get("Set-Cookie"));
    var cookie = cookieOpt.orElseThrow();

    // Add the cookie to next request.
    headers = new HttpHeaders();
    headers.add("Cookie", cookie.get(0));
    return new HttpEntity<>(body, headers);
  }
}
