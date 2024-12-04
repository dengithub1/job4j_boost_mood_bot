package ru.job4j.bmb.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.bmb.model.Award;

public interface AwardRepository extends CrudRepository<Award, Long> {
}
