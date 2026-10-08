package pro.sky.telegrambot.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idNtfTask;
    @Column(name = "chat_id")
    private long chatId;
    @Column(name = "message")
    private String message;
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    public NotificationTask() {}

    public NotificationTask(long chatId, String message, LocalDateTime dateTime) {
        this.chatId = chatId;
        this.message = message;
        this.dateTime = dateTime;
    }

    public Integer getIdNtfTask() {
        return idNtfTask;
    }

    public void setIdNtfTask(Integer idNtfTask) {
        this.idNtfTask = idNtfTask;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(idNtfTask, that.idNtfTask);
    }

    @Override
    public int hashCode() {
        return idNtfTask != null ? idNtfTask.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "idNtfTask=" + idNtfTask +
                ", chatId=" + chatId +
                ", message='" + message + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
