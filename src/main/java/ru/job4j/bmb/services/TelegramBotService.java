package ru.job4j.bmb.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.job4j.bmb.SentContentException;
import ru.job4j.bmb.content.Content;

@Component
@Service
public class TelegramBotService extends TelegramLongPollingBot implements SentContent {
    private final BotCommandHandler handler;
    private final String botName;

    public TelegramBotService(@Value("${telegram.bot.name}") String botName,
                              @Value("${telegram.bot.token}") String botToken,
                              BotCommandHandler handler) {
        super(botToken);
        this.botName = botName;
        this.handler = handler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handler.handleCallback(update.getCallbackQuery())
                    .ifPresent(content -> sent(content));
        } else if (update.hasMessage() && update.getMessage().getText() != null) {
            System.out.println(update.getMessage().getText());
            handler.commands(update.getMessage())
                    .ifPresent(content -> sent(content));
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void sent(Content content) {
        if (content.getPhoto() != null) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setPhoto(content.getPhoto());
            sendPhoto.setChatId(content.getChatId());
            try {
                execute(sendPhoto);
            } catch (TelegramApiException e) {
                throw new SentContentException(e.getMessage(), e);
            }
        } else if (content.getMarkup() != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setReplyMarkup(content.getMarkup());
            sendMessage.setChatId(content.getChatId());
            sendMessage.setText("df");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new SentContentException(e.getMessage(), e);
            }
        } else if (content.getText() != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(content.getText());
            sendMessage.setChatId(content.getChatId());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new SentContentException(e.getMessage(), e);
            }
        }
    }
}
