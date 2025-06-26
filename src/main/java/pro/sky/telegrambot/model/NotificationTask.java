package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reminder")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "chat_id")
    private Long chatId;
    private String text;
    @Column(name = "reminder_time")
    private LocalDateTime reminderTime;

    public NotificationTask() {
    }

    public NotificationTask(Long chatId, String text, LocalDateTime reminderTime) {
        this.chatId = chatId;
        this.text = text;
        this.reminderTime = reminderTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = this.chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }
}
