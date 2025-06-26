package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repositories.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationSchedulerService {
    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramSenderService telegramSenderService;

    public NotificationSchedulerService(NotificationTaskRepository notificationTaskRepository, TelegramSenderService telegramSenderService) {
        this.notificationTaskRepository = notificationTaskRepository;
        this.telegramSenderService = telegramSenderService;
    }

    @Scheduled(cron = "0 * * * * *")//каждый час, каждую минуту, в ноль секунд
    public void sendNotifications() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasks = notificationTaskRepository.findAllByReminderTime(now);

        for (NotificationTask task : tasks) {
            telegramSenderService.sendMessage(task.getChatId(), task.getText());
            //подумала, чтобы не захламлять БД старыми напоминалками, удалять после отправки
            notificationTaskRepository.delete(task);
        }
    }

}
