package vn.ecall.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.ecall.config.EmailProperties;
import vn.ecall.service.dto.ContactDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private final EmailProperties emailProperties;

    private final JavaMailSender javaMailSender;

    public MailService(EmailProperties emailProperties, JavaMailSender javaMailSender) {
        this.emailProperties = emailProperties;
        this.javaMailSender = javaMailSender;
    }

    /**
     * @param to          email address which receives the email
     * @param subject     the sender - fetch from "from" property in application.yml.
     * @param content     content of the email - get from template and complemented by form information.
     * @param isMultipart is multipart response or not
     * @param isHtml      content is html or not
     */
    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
                "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart,
                isHtml,
                to,
                subject,
                content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(emailProperties.getFrom());
            message.setSubject(subject);

            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    public SimpleMailMessage templateSimpleMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Yêu cầu liên hệ của người dùng với các thông tin sau: \r\n" +
                "- Email: %s\r\n" +
                "- Họ tên: %s\r\n" +
                "- Số điện thoại liên hệ: %s\r\n" +
                "- Nội dung: %s\r\n");
        return message;
    }

    @Async
    public void sendContactEmail(ContactDTO contact) {
        String to = emailProperties.getTo();
        log.debug("Sending request contact mail to {}", to);
        String subject = contact.getSubject();
        String content = String.format(Objects.requireNonNull(templateSimpleMessage().getText()), contact.getEmail(), contact.getName(), contact.getPhone(), contact.getContent());
        sendEmail(to, subject, content, false, false);
    }
}
