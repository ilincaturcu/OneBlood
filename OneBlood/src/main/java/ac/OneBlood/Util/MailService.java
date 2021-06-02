package ac.OneBlood.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String emailContent, String email) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);

        msg.setSubject("Centrul de Transfuzii Iasi - One Blood");
        msg.setText(emailContent);

        javaMailSender.send(msg);

        System.out.println("trimis");
    }
}
