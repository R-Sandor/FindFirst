package dev.findfirst.users.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.mail.internet.MimeBodyPart;
import java.time.ZonedDateTime;
import org.eclipse.angus.mail.smtp.SMTPMessage;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MailHogContentMessage(
    String ID,
    MailHogPath Path,
    MailHogContent Content,
    ZonedDateTime created,
    MimeBodyPart MIME,
    SMTPMessage raw) {}
