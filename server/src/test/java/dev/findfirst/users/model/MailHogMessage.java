package dev.findfirst.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MailHogMessage(int total, int count, int start, MailHogContentMessage[] items) {}
