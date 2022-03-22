package ru.liga.telegram;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.JFreeChart;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.liga.ForecastStorage;
import ru.liga.exceptions.NotFoundDataException;
import ru.liga.source.CurrencyName;
import ru.liga.source.DataSource;
import ru.liga.source.FileDataSource;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Класс отвечающий за действия на запросы пользователей,
 * которые не являются командами.
 */
@Slf4j
public class RateCommand {

    /**
     * Создаёт ответ пользователю на основе его запроса.
     *
     * @param userName имя пользователя
     * @param request  запроса
     */
    public void execute(AbsSender absSender, String chatId, String userName, TelegramRateRequest request) {
        ForecastStorage forecastStorage = new ForecastStorage();
        log.debug("Пользователь {}. Хранилище для результатов прогнозирования создано.", userName);

        try {
            for (CurrencyName currencyName : request.getCurrencyNames()) {
                DataSource dataSource = new FileDataSource(currencyName);
                log.debug("Пользователь {}. Исходные данные прочитаны.", userName);

                Map<LocalDate, Double> forecast = request.getAlgorithm().getForecast(
                        dataSource.getData(),
                        request.getDate(),
                        request.getCountDays()
                );
                log.debug("Пользователь {}. Алгоритм {} отработал.",
                        userName, request.getAlgorithm().getClass().getName());

                forecastStorage.putForecast(currencyName, forecast);
                log.debug("Прогноз помещён в хранилище");
            }
        } catch (NotFoundDataException e) {
            log.debug("Алгоритм {}. Данные не были найдены.",
                    request.getAlgorithm().getClass().getName());

            sendText(absSender, chatId, userName, e.getMessage());
        }

        switch (request.getOutput()) {
            case "list":
                sendText(absSender, chatId, userName, forecastStorage.getForecastList());
                log.debug("Пользователь {} получил результат запроса в виде списка.", userName);
                break;
            case "graph":
                sendGraph(absSender, chatId, userName, forecastStorage.getForecastGraph());
                log.debug("Пользователь {} получил результат запроса в виде графика.", userName);
        }
    }

    /**
     * Отправка текста пользователю.
     *
     * @param chatId   id чата
     * @param userName имя пользователя
     * @param text     текст
     */
    private void sendText(AbsSender absSender, String chatId, String userName, String text) {
        try {
            absSender.execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .build()
            );
        } catch (TelegramApiException e) {
            log.error("Ошибка {}. Отправка текста. Пользователь: {}",
                    e.getMessage(), userName, e);
        }
    }

    /**
     * Отправка графика пользователю.
     *
     * @param chatId   id чата
     * @param userName имя пользователя
     * @param chart    график
     */
    private void sendGraph(AbsSender absSender, String chatId, String userName, JFreeChart chart) {
        try {
            File file = File.createTempFile("image", ".tmp");
            ImageIO.write(chart.createBufferedImage(800, 700), "png", file);
            log.debug("Пользователь {}. Временный файл с графиком создан.", userName);

            absSender.execute(
                    SendPhoto.builder()
                            .chatId(chatId)
                            .photo(new InputFile(file))
                            .build()
            );
        } catch (TelegramApiException e) {
            log.error("Ошибка {}. Отправка графика. Пользователь: {}",
                    e.getMessage(), userName, e);
        } catch (IOException e) {
            log.error("Ошибка {}. Создание временного файла. Пользователь: {}",
                    e.getMessage(), userName, e);
        }
    }
}
