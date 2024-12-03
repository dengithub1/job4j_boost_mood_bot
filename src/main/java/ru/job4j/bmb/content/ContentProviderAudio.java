package ru.job4j.bmb.content;

import org.telegram.telegrambots.meta.api.objects.InputFile;
import ru.job4j.bmb.model.Content;

import java.io.File;

public class ContentProviderAudio implements ContentProvider {

    @Override
    public Content byMood(Long chatId, Long moodId) {
        var content = new Content(chatId);
        content.setAudio(new InputFile(new File("./audio/music.mp3")));
        return content;
    }
}
