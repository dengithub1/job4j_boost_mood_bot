package ru.job4j.bmb;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.job4j.bmb.model.Award;
import ru.job4j.bmb.model.Mood;
import ru.job4j.bmb.model.MoodContent;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodContentRepository;
import ru.job4j.bmb.repository.MoodRepository;

import java.util.ArrayList;
import java.util.stream.Collectors;

@EnableScheduling
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner localDataBase(MoodRepository moodRepository,
                                    MoodContentRepository moodContentRepository,
                                    AwardRepository awardRepository) {
        return args -> {
            var moods = moodRepository.findAll();
            if (!moods.isEmpty()) {
                return;
            }
            var data = new ArrayList<MoodContent>();
            data.add(new MoodContent(new Mood("Воодушевленное настроение \\U0001F32A", true),
                    "Великолепно! Вы чувствуете себя на высоте. Продолжайте в том же духе."));
            data.add(new MoodContent(new Mood("Успокоение и гармония \\U0001F64D", true),
                    "Потрясающе! Вы в состоянии внутреннего мира и гармонии."));
            data.add(new MoodContent(new Mood("В состоянии комфорта \\U0001F60A", true),
                    "Потрясающе! Отлично! Вы чувствуете себя уютно и спокойно."));
            data.add(new MoodContent(new Mood("Легкое волнение \\U0001F388", true),
                    "Замечательно! Немного волнения добавляет жизни краски."));
            data.add(new MoodContent(new Mood("Сосредоточенное настроение \\U0001F64C", true),
                    "Хорошо! Ваш фокус на высоте, используйте это время эффективно."));
            data.add(new MoodContent(new Mood("Тревожное настроение \\U0001F62D", false),
                    "Не волнуйтесь, всё пройдет. Попробуйте расслабиться и найти источник вашего беспокойства."));
            data.add(new MoodContent(new Mood("Разочарованное настроение \\U0001F62E", false),
                    "Бывает. Не позволяйте разочарованию сбить вас с толку, всё наладится."));
            data.add(new MoodContent(new Mood("Усталое настроение \\U0001F443", false),
                    "Похоже, вам нужен отдых. Позаботьтесь о себе и отдохните."));
            data.add(new MoodContent(new Mood("Вдохновенное настроение \\U0001F9B4", true),
                    "Потрясающе! Вы полны идей и энергии для их реализации."));
            data.add(new MoodContent(new Mood("Раздраженное настроение \\U0001F621", false),
                    "Попробуйте успокоиться и найти причину раздражения, чтобы исправить ситуацию."));
            moodRepository.saveAll(data.stream().map(MoodContent::getMood).collect(Collectors.toList()));
            moodContentRepository.saveAll(data);

            var awards = new ArrayList<Award>();
            awards.add(new Award("Смайлик дня", "За 1 день хорошего настроения", 1));
            awards.add(new Award("Настроение недели", "За 7 последовательных дней хорошего или отличного настроения", 7));
            awards.add(new Award("Персонализированные рекомендации", "За 5 день хорошего настроения", 5));
            awardRepository.saveAll(awards);
        };
    }
}