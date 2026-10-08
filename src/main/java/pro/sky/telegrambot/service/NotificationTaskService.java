package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationTaskService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);

    private static final Pattern TASK_PATTERN = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}\\s\\d{2}:\\d{2})(\\s+)(.+)");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private final NotificationTaskRepository repository;
    private final TelegramBot telegramBot;

    @Autowired
    public NotificationTaskService(NotificationTaskRepository repository, TelegramBot telegramBot) {
        this.repository = repository;
        this.telegramBot = telegramBot;
    }

    // Задание 2.4: Парсинг и сохранение
    public void parseAndSave(String text, long chatId) {
        Matcher matcher = TASK_PATTERN.matcher(text.trim());
        if (matcher.matches()) {
            String dateTimeString = matcher.group(1);
            String messageText = matcher.group(3);

            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, FORMATTER);
                NotificationTask task = new NotificationTask(chatId, messageText, dateTime);
                repository.save(task);
                telegramBot.execute(new SendMessage(chatId, "✅ Напоминание успешно создано на " + dateTimeString));
                logger.info("Сохранено напоминание для чата {} на {}", chatId, dateTime);
            } catch (Exception e) {
                telegramBot.execute(new SendMessage(chatId, "❌ Ошибка формата даты. Используйте формат: dd.MM.yyyy HH:mm Текст напоминания"));
                logger.error("Ошибка парсинга даты для чата {}: {}", chatId, text, e);
            }
        } else {
            telegramBot.execute(new SendMessage(chatId, "❌ Не удалось распознать формат. Пример: 01.01.2022 20:00 Сделать домашнюю работу"));
        }
    }

    // Задание 2.5: Поиск задач на текущую минуту
    public List<NotificationTask> findTasksForCurrentMinute() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
        return repository.findByDateTimeBetween(now, now.plusMinutes(1));
    }

    // Задание 2.6: Рассылка уведомлений
    public void sendNotifications(List<NotificationTask> tasks) {
        for (NotificationTask task : tasks) {
            telegramBot.execute(new SendMessage(task.getChatId(), "⏰ Напоминание: " + task.getMessage()));
            logger.info("Отправлено уведомление в чат {} с текстом: {}", task.getChatId(), task.getMessage());
        }
    }

    @Transactional
    public void deleteTask(NotificationTask task) {
        repository.delete(task);
    }
}
