package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.MoodContent;

public interface MoodContentRepository extends CrudRepository<MoodContent, Long> {
}
