package dev.findfirst.users.model;

import java.time.ZonedDateTime;

import org.eclipse.angus.mail.smtp.SMTPMessage;

import jakarta.mail.internet.MimeBodyPart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MailHogContentMessage(String ID, MailHogPath Path, MailHogContent Content, ZonedDateTime created,
		MimeBodyPart MIME, SMTPMessage raw) {
}
