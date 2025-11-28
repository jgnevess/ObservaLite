package com.example.ObservaLite.services.auth;

import com.example.ObservaLite.entities.auth.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    @Autowired
    private Environment env;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendActivationEmail(User user, String activationLink) {
        String template = loadTemplate();

        Map<String, String> vars = Map.of(
                "userName", user.getFirstName(),
                "activationLink", activationLink,
                "activationCode", user.getActivateCode(),
                "daysTrial", "7",
                "docsUrl", "https://observa-lite/docs",
                "privacyUrl", "https://observa-lite/privacy",
                "unsubscribeUrl", "https://observa-lite/unsubscribe",
                "codeExpiryMinutes", "30"
        );

        String html = applyVariables(template, vars);

        sendHtml(user.getValidEmail(), html);
    }


    private String loadTemplate() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("templates/" + "activate-mail-template.html")) {
            assert is != null;
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar template " + "activate-mail-template.html", e);
        }
    }

    private String applyVariables(String template, Map<String, String> values) {
        String result = template;
        for (Map.Entry<String, String> e : values.entrySet()) {
            result = result.replace("{{" + e.getKey() + "}}", e.getValue());
        }
        return result;
    }

    private void sendHtml(String to, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(to);
            helper.setSubject("Ative sua conta");
            helper.setText(html, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }
}
