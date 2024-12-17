package ru.job4j.bmb.services;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.model.Achievement;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;
import ru.job4j.bmb.repository.AchievementRepository;
import ru.job4j.bmb.repository.AwardRepository;
import ru.job4j.bmb.repository.MoodLogRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AchievementService implements ApplicationListener<UserEvent> {

    private final AchievementRepository achievementRepository;
    private final MoodLogRepository moodLogRepository;
    private final AwardRepository awardRepository;
    private final SentContent sentContent;

    public AchievementService(AchievementRepository achievementRepository,
                              MoodLogRepository moodLogRepository,
                              AwardRepository awardRepository,
                              SentContent sentContent) {
        this.moodLogRepository = moodLogRepository;
        this.achievementRepository = achievementRepository;
        this.awardRepository = awardRepository;
        this.sentContent = sentContent;
    }

    @Transactional
    @Override
    public void onApplicationEvent(UserEvent event) {
        var user = event.getUser();
        var achievements = new ArrayList<Achievement>();
        var goodMoodLog = moodLogRepository.findByUserId(user.getClientId()).stream()
                .filter(moodLog -> moodLog.getMood().isGood())
                .sorted(Comparator.comparingLong(MoodLog::getCreatedAt))
                .toList();

        var maxGoodDays = countMaxDaysMood(goodMoodLog);
        var awards = awardRepository.findAllByDaysLessThanEqual(maxGoodDays);
        var existsAwards = achievementRepository.findAllByUserId(user.getClientId()).stream()
                .map(x -> x.getAward())
                .toList();
        awards.removeAll(existsAwards);

        awards.forEach(
                award -> {
                    var achievement = new Achievement();
                    achievement.setUser(user);
                    achievement.setAward(award);
                    achievement.setCreateAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                    achievements.add(achievement);
                });
        if (!achievements.isEmpty()) {
            achievementRepository.saveAll(achievements);
            sendAchievements(user, achievements);
        }
    }

    private int countMaxDaysMood(List<MoodLog> moodLogs) {
        int count = 1;
        int maxGoodDays = 1;
        for (int i = 0; i < moodLogs.size() - 1; i++) {
            if (LocalDate.ofInstant(Instant.ofEpochSecond(moodLogs.get(i).getCreatedAt()), ZoneOffset.UTC).plusDays(1)
                    .isEqual(LocalDate.ofInstant(Instant.ofEpochSecond(moodLogs.get(i + 1).getCreatedAt()), ZoneOffset.UTC))) {
                count++;
            } else {
                count = 0;
            }
            maxGoodDays = Math.max(maxGoodDays, count);
        }
        return maxGoodDays;
    }

    void sendAchievements(User user, List<Achievement> achievements) {
        Content content = new Content(user.getChatId());
        StringBuilder stringBuilder = new StringBuilder("Ваши достижения:\n");
        for (Achievement achievement : achievements) {
            stringBuilder.append(achievement.getAward().getTitle() + "\n");
        }
        content.setText(stringBuilder.toString());
        sentContent.sent(content);
    }
}

