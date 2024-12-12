package ru.job4j.bmb.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.UserRepository;

import java.util.Optional;

@Service
public class BotCommandHandler {
    public final UserRepository userRepository;
    public final MoodService moodService;
    public final TgUI tgUI;

    public BotCommandHandler(UserRepository userRepository,
                             MoodService moodService,
                             TgUI tgUI) {
        this.userRepository = userRepository;
        this.moodService = moodService;
        this.tgUI = tgUI;
    }

    Optional<Content> commands(Message message) {
        String command = message.getText();
        long chatId = message.getChatId();
        long userId = message.getFrom().getId();
        switch (command) {
            case "/start" -> {
                return handleStartCommand(chatId, userId);
            }
            case "/week_mood_log" -> {
                return moodService.weekMoodLogCommand(chatId, chatId);
            }
            case "/month_mood_log" -> {
                return moodService.monthMoodLogCommand(chatId, chatId);
            }
            case "/award" -> {
                return moodService.awards(chatId, chatId);
            }
            default -> {
                return Optional.empty();
            }
        }
    }

    Optional<Content> handleCallback(CallbackQuery callback) {
        var moodId = Long.valueOf(callback.getData());
        var user = userRepository.findByClientId(callback.getFrom().getId());
        return user.map(v -> moodService.chooseMood(v, moodId));
    }

    Optional<Content> handleStartCommand(long chatId, Long clientId) {
        var user = new User();
        user.setChatId(chatId);
        user.setClientId(clientId);
        userRepository.save(user);
        System.out.println(userRepository.findAll());
        var content = new Content(chatId);
        content.setText("Как настроение?");
        content.setMarkup(tgUI.buildButtons());
        return Optional.of(content);
    }
}
