package ru.job4j.bmb.repository;

import org.springframework.test.fake.CrudRepositoryFake;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MoodLogFakeRepository extends CrudRepositoryFake<MoodLog, Long> implements MoodLogRepository {

    public List<MoodLog> findAll() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public List<MoodLog> findByUserId(Long userId) {
        return List.of();
    }

    @Override
    public List<User> findUsersWhoDidNotVoteToday(long start, long end) {
        List<User> users = findAll().stream()
                .map(MoodLog::getUser)
                .distinct()
                .collect(Collectors.toList());
        List<User> listToday = findAll().stream()
                .filter(moodLog -> moodLog.getCreatedAt() >= start)
                .filter(moodLog -> moodLog.getCreatedAt() <= end)
                .map(MoodLog::getUser)
                .distinct()
                .toList();
        users.removeAll(listToday);
        return users;
    }
}

