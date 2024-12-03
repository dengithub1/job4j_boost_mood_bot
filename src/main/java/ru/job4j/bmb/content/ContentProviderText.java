package ru.job4j.bmb.content;

import org.springframework.stereotype.Component;
import ru.job4j.bmb.model.Content;

@Component
public class ContentProviderText implements ContentProvider {

    @Override
    public Content byMood(Long chatId, Long moodId) {
        var content = new Content(chatId);
        content.setText("Text");
        return content;
    }
}
