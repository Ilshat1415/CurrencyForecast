package ru.liga;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.telegram.Bot;

@Slf4j
public class CurrencyForecast {

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot("CurrencyForecastBot", "5174504068:AAEuByska9ueuUF-EjPzorCOo5wB2AzrCCg"));
        } catch (TelegramApiException e) {
            log.error("Ошибка {}. Создание телеграмм-бота.", e.getMessage(), e);
        }
    }
}
