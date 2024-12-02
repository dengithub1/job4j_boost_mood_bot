package ru.job4j.bmb.services;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class TelegramBotService {

    @PostConstruct
    public void init() {
        System.out.println("Bean is going through init.");
    }

    @PostConstruct
    public void destroy() {
        System.out.println("Bean will be destroyed now.");
    }
}
