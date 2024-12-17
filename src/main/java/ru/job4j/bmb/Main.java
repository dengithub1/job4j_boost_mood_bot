package ru.job4j.bmb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodContentRepository;
import ru.job4j.bmb.repository.MoodRepository;
import ru.job4j.bmb.services.TelegramBotService;

import java.util.ArrayList;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner initTelegramApi(ApplicationContext ctx) {
        return args -> {
            var bot = ctx.getBean(TelegramBotService.class);
            var botsApi = new TelegramBotsApi(DefaultBotSession.class);
            try {
                botsApi.registerBot(bot);
                System.out.println("Бот успешно зарегистрирован");
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        };
    }

    @Bean
    CommandLineRunner loadDatabase(MoodRepository moodRepository,
                                   MoodContentRepository moodContentRepository,
                                   AwardRepository awardRepository) {
        return args -> {
            var moods = moodRepository.findAll();
            if (!moods.isEmpty()) {
                return;
            }
            var data = new ArrayList<MoodContent>();
            data.add(new MoodContent(
                    new Mood("Счастливейший на свете \uD83D\uDE0E", true),
                    "Невероятно! Вы сияете от счастья, продолжайте радоваться жизни."));

            moodRepository.saveAll(data.stream().map(MoodContent::getMood).toList());
            moodContentRepository.saveAll(data);

            var awards = new ArrayList<Award>();
            awards.add(new Award("Смайлик дня", "За 1 день хорошего настроения.", 1));

            awardRepository.saveAll(awards);
        };
    }
}