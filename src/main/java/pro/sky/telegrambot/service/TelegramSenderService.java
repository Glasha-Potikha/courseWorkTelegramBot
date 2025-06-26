package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class TelegramSenderService {
    private final TelegramBot telegramBot;
    public TelegramSenderService(TelegramBot telegramBot){
        this.telegramBot = telegramBot;
    }
    public void sendMessage(Long chatId, String text){
        telegramBot.execute(new SendMessage(chatId, text));
    }
}
