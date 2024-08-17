package dev.findfirst.users.model;

import java.util.Map;

import jakarta.mail.internet.MimeBodyPart;

public record MailHogContent(Map<String, String[]> Headers, String Body, int Size, MimeBodyPart MIME) {
}
