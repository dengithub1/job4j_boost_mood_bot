package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.Mood;

public interface MoodRepository extends CrudRepository<Mood, Long> {
}
