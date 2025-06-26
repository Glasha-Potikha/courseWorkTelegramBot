package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repositories.NotificationTaskRepository;
import pro.sky.telegrambot.service.TelegramSenderService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @Autowired
    private TelegramSenderService telegramSenderService;
    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Autowired
    private NotificationTaskRepository notificationTaskRepository;

    @Override
    public int process(List<Update> updates) {
        Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            if (update.message() != null && update.message().text() != null) {
                String messageText = update.message().text();
                Long chatId = update.message().chat().id();

                if (messageText.equals("/start")) {
                    telegramBot.execute(new com.pengrad.telegrambot.request.SendMessage(
                            chatId,
                            "Дратути :3 \n сюда ты можешь присылать свои напоминалки в формате - <<01.01.2022 20:00 Сделать домашнюю работу>> " +
                                    "\n и я тебе напомню о ней в назначенное время"

                    ));
                } else {
                    Matcher matcher = pattern.matcher(messageText);
                    if (matcher.matches()) {
                        String dateTimeString = matcher.group(1);
                        String taskText = matcher.group(3);
                        //строка во время LocalDateTime
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
                        //сохраняем напоминалку
                        NotificationTask task = new NotificationTask(chatId, taskText, dateTime);
                        notificationTaskRepository.save(task);
                        telegramSenderService.sendMessage(chatId, "Поняль-принял, в нужное время тебе напомню");

                    } else {
                        telegramSenderService.sendMessage(chatId, "Я тебя не понимаю.. формат напоминалки неверный");
                    }
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
