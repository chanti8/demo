package com.example.sendEmail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig {

    @Bean
    public SimpleMailMessage emailTemplate()
    {
        SimpleMailMessage message = new SimpleMailMessage();
        //message.setTo("ChantiNaik.Banavath@team.neustar");
        message.setTo("idiot8b@gmail.com");
        message.setFrom("duminy2010226@gmail.com");
        message.setSubject("NeuSamiksha Dashboard Report");
        message.setText("Please find the attached file!!!");
        return message;
    }
}
