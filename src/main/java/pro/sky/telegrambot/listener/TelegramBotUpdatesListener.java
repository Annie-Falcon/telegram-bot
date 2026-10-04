package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private static final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    // Константа для команды /start, чтобы избежать опечаток
    private static final String START_COMMAND = "/start";
    // Приветственный текст
    private static final String WELCOME_TEXT = "Привет! Я рад видеть вас здесь. Этот бот создан на Java с использованием библиотеки com.pengrad:telegrambot.";

    @Autowired
    private TelegramBot telegramBot;

    @Autowired private
    NotificationTaskService notificationService; // Внедряем сервис

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            // Проверяем, что у update есть сообщение и это именно текстовое сообщение
            if (update.message() != null && update.message().text() != null) {
                Message message = update.message();
                String text = message.text();
                long chatId = message.chat().id();

                // Сравниваем текст с командой /start
                if (START_COMMAND.equals(message.text())) {
                    // Отправляем ответное сообщение через экземпляр бота
                    telegramBot.execute(new SendMessage(chatId, WELCOME_TEXT));
                    logger.info("Отправлено приветствие пользователю {} в чате {}",
                            message.from().username(), chatId);
                } else {
                    notificationService.parseAndSave(text, chatId);
                }
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
