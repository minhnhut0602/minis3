package com.pksprojects.minis3.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Simple Service providing basic API for sending emails.
 * Created by PKS on 4/10/17.
 */
@Service
public class EmailService {

    private static final Logger logger = LogManager.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends email with the given parameters
     * @param from address
     * @param to address
     * @param subject of email
     * @param text email text
     */
    public void sendEmail(String from, String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        }catch (MailException e) {
            logger.error(String.format("Error while sending email from: %s,to: %s, text: %s", from, to, text), e);
        }
    }
}
