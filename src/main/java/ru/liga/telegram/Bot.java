package ru.liga.telegram;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.telegram.commands.service.HelpCommand;
import ru.liga.telegram.commands.service.StartCommand;

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
    private final RateCommand rateCommand;

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

        rateCommand = new RateCommand();
        log.debug("Класс обработки rate запросов");
        register(new StartCommand("start", "Старт"));
        log.debug("Команда start создана");
        register(new HelpCommand("help", "Помощь"));
        log.debug("Команда help создана");

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
                String userName = Utils.getUserName(message);
                String text = message.getText();
                Long chatId = message.getChatId();

                try {
                    TelegramRateRequest request = new TelegramRateRequest(text);
                    log.info("Пользователь {}. Запрос {} принят на обработку.", userName, text);

                    rateCommand.execute(this, chatId.toString(), userName, request);
                    log.info("Пользователь {}. Запрос {} обработан.", userName, text);

                } catch (IllegalArgumentException e) {
                    log.debug("Пользователь {}. Запрос не был принят.", userName);
                    sendMessage(chatId, userName, e.getMessage());
                }
            }
        }
    }

    /**
     * Отправка сообщения пользователю.
     *
     * @param chatId   id чата
     * @param userName имя пользователя
     * @param text     текст сообщения
     */
    private void sendMessage(Long chatId, String userName, String text) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Ошибка {}. Сообщение, не являющееся командой. Пользователь: {}",
                    e.getMessage(), userName, e);
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
