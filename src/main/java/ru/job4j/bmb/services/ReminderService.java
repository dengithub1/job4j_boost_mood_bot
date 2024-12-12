package ru.job4j.bmb.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.repository.UserRepository;

@Service
public class ReminderService {

    private final TelegramBotService telegramBotService;
    private final UserRepository userRepository;

    public ReminderService(TelegramBotService telegramBotService, UserRepository userRepository) {
        this.telegramBotService = telegramBotService;
        this.userRepository = userRepository;
    }

    @Scheduled(fixedRateString = "${remind.period}")
    public void ping() {
        for (var user : userRepository.findAll()) {
            var content = new Content(user.getChatId());
            content.setText("Ping");
            telegramBotService.sent(content);
        }
    }
}
