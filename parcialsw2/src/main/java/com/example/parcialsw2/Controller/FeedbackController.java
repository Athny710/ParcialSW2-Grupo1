package com.example.parcialsw2.Controller;
import com.example.parcialsw2.Config.Feedback;
import com.example.parcialsw2.Config.EmailCfg;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.xml.bind.ValidationException;

@RestController
@RequestMapping("/confirm")
public class FeedbackController {

    private EmailCfg emailCfg;
    public FeedbackController(EmailCfg emailCfg){
        this.emailCfg = emailCfg;
    }
    @PostMapping("/reestablecer")
    public String sendMessage(@RequestBody Feedback feedback, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException("Mensaje no válido");
        }
        //Creamos un envio de email
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailCfg.getHost());
        mailSender.setPort(this.emailCfg.getPort());
        mailSender.setUsername(this.emailCfg.getUsername());
        mailSender.setPassword(this.emailCfg.getPassword());
        //Creamos una instancia de email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(feedback.getEmail());
        mailMessage.setTo("abc@feedback.com");
        mailMessage.setSubject("Nueva solicitud de " + feedback.getNombre());
        mailMessage.setText(feedback.getMensaje());
        //Enviar email
        mailSender.send(mailMessage);
    return "iniciarSesion";

    }
}
