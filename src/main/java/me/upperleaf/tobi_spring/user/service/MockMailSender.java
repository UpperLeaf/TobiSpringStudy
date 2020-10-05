package me.upperleaf.tobi_spring.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.ArrayList;
import java.util.List;

public class MockMailSender implements MailSender {
    private List<String> request = new ArrayList<>();
    private String host;

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        request.add(simpleMessage.getTo()[0]);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }

    public List<String> getRequest() {
        return request;
    }
}
