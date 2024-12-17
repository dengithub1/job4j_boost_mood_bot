package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.bmb.model.MoodLog;
import ru.job4j.bmb.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface MoodLogRepository extends CrudRepository<MoodLog, Long> {
    @Override
    List<MoodLog> findAll();

    List<MoodLog> findByUserId(Long userId);

    default List<User> findUsersWhoDidNotVoteToday(long start, long end) {
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