package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Integer> {
    // Ищем задачи, время которых больше или равно началу минуты и строго меньше начала следующей минуты
    @Query("SELECT n FROM NotificationTask n WHERE n.dateTime >= :start AND n.dateTime < :end")
    List<NotificationTask> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);
}
