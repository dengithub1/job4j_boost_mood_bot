package ru.job4j.bmb.services;

import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class BotCommandHandler implements BeanNameAware {
    private String name;

    @Override
    public void setBeanName(String name) {
        this.name = name;
        System.out.println(this.name);
    }
}
