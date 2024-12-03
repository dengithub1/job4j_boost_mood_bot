package ru.job4j.bmb.content;

import ru.job4j.bmb.model.Content;

public interface ContentProvider {
    Content byMood(Long chatId, Long moodId);
}
