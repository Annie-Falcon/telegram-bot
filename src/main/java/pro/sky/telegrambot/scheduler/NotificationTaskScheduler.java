package pro.sky.telegrambot.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class NotificationTaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationTaskScheduler.class);

    private final NotificationTaskService notificationTaskService;

    public NotificationTaskScheduler(NotificationTaskService notificationTaskService) {
        this.notificationTaskService = notificationTaskService;
    }

    // cron "0 0/1 * * * *" означает: в 0 секунд, каждую 1 минуту
    @Scheduled(cron = "0 0/1 * * * *")
    public void checkAndSendNotifications() {
        logger.info("Запуск проверки напоминаний на минуту: {}",
                LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES));

        List<NotificationTask> tasks = notificationTaskService.findTasksForCurrentMinute();

        if (!tasks.isEmpty()) {
            logger.info("Найдено {} задач для отправки", tasks.size());

            notificationTaskService.sendNotifications(tasks);

            // после отправки удаляем задачи, чтобы бот не слал их каждую минуту
            tasks.forEach(notificationTaskService::deleteTask);
        }
    }
}