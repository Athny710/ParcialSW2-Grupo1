package com.example.parcialsw2.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private EmailCfg emailCfg;

    public FeedbackController(EmailCfg emailCfg){
        this.emailCfg = emailCfg;
    }

    @PostMapping
    public String sendFeedback(@RequestBody Feedback feedback){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailCfg.getHost());
        mailSender.setPort(this.emailCfg.getPort());
        mailSender.setUsername(this.emailCfg.getUsername());
        mailSender.setPassword(this.emailCfg.getPassword());

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(feedback.getEmail());
        mailMessage.setTo("a20145976@pucp.pe");
        mailMessage.setSubject("new feedback from");
        mailMessage.setText("dsaldhkaslkjlaskjalkfj");

        mailSender.send(mailMessage);
        return "/recuperar";
    }
}
