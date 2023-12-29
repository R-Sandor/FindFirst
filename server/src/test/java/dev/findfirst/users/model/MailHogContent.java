package dev.findfirst.users.model;

import jakarta.mail.internet.MimeBodyPart;
import java.util.Map;

public record MailHogContent(
    Map<String, String[]> Headers, String Body, int Size, MimeBodyPart MIME) {}
