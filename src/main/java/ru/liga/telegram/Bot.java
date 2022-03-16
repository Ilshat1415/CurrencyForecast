package ru.liga.telegram;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.telegram.commands.service.HelpCommand;
import ru.liga.telegram.commands.service.StartCommand;

import java.io.File;

/**
 * Это телеграмм-бот.
 */
@Slf4j
public class Bot extends TelegramLongPollingCommandBot {
    /**
     * Имя телеграмм-бота
     */
    private final String BOT_NAME;
    /**
     * Уникальный токен телеграмм-бота
     */
    private final String BOT_TOKEN;
    /**
     * Объект обрабатывающий запросы - не команды.
     */
    private final NonCommand nonCommand;

    /**
     * Создание телеграмм-бота.
     *
     * @param BOT_NAME  имя телеграмм-бота
     * @param BOT_TOKEN уникальный токен телеграмм-бота
     */
    public Bot(String BOT_NAME, String BOT_TOKEN) {
        super();
        log.debug("Конструктор супер класса отработал.");
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;
        log.debug("Имя и токен присвоены");

        nonCommand = new NonCommand();
        log.debug("Класс обработки сообщения, не являющегося командой, создан");
        register(new StartCommand("start", "Старт"));
        log.debug("Команда start создана");
        register(new HelpCommand("help", "Помощь"));
        log.debug("Команда start создана");

        log.info("Бот создан!");
    }

    /**
     * Реакция бота на запрос, не являющийся командой.
     *
     * @param update запрос пользователя
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                Object answer = nonCommand.execute(Utils.getUserName(message), message.getText());
                log.info("Пользователь {}. Запрос {} обработан.", Utils.getUserName(message), message.getText());

                setAnswer(message.getChatId(), Utils.getUserName(message), answer);
            }
        }
    }

    /**
     * Отправка ответа пользователю.
     *
     * @param chatId   id чата
     * @param userName имя пользователя
     * @param result   результат прогноза
     */
    private void setAnswer(Long chatId, String userName, Object result) {
        try {
            if (result instanceof String) {
                SendMessage answer = new SendMessage();
                answer.setText((String) result);
                answer.setChatId(chatId.toString());
                execute(answer);
            } else if (result instanceof File) {
                SendPhoto answer = new SendPhoto();
                answer.setPhoto(new InputFile((File) result));
                answer.setChatId(chatId.toString());
                execute(answer);
            }
            log.info("Ответ отправлен пользователю {}.", userName);
        } catch (TelegramApiException e) {
            log.error("Ошибка {}. Сообщение, не являющееся командой. Пользователь: {}", e.getMessage(),
                    userName, e);
        }
    }

    /**
     * Возвращает имя бота.
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    /**
     * Возвращает уникальный токен бота.
     *
     * @return уникальный токен бота
     */
    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}
