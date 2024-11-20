package com.vishal.project.moveInSync.moveInSyncApp.services.impl;

import com.vishal.project.moveInSync.moveInSyncApp.services.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendEmail(String toEmail, String subject, String body) {
        try {
            // Create a MimeMessage object
            jakarta.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Create MimeMessageHelper with HTML support
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true); // true indicates that the email content is HTML

            // Send the email
            javaMailSender.send(mimeMessage);
            log.info("HTML email sent to " + toEmail);

        } catch (MailException e) {
            log.error("Cannot send mail: " + e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmails(String[] toEmail, String subject, String body) {
        try {
            // Create a MimeMessage object
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            // Create MimeMessageHelper with HTML support
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true); // true indicates that the email content is HTML

            // Send the email
            javaMailSender.send(mimeMessage);
            log.info("HTML email sent to multiple recipients");

        } catch (MailException e) {
            log.error("Cannot send mail: " + e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
