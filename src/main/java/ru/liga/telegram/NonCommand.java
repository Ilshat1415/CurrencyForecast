package ru.liga.telegram;

import lombok.extern.slf4j.Slf4j;
import ru.liga.Controller;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс отвечающий за действия на запросы пользователей,
 * которые не являются командами.
 */
@Slf4j
public class NonCommand {
    private static final Pattern RATE_PATTERN = Pattern.compile("[Rr]ate +\\S+");
    private static final Pattern DATE_PATTERN = Pattern.compile("-date +\\S+");
    private static final Pattern PERIOD_PATTERN = Pattern.compile("-period +\\S+");
    private static final Pattern ALG_PATTERN = Pattern.compile("-alg +\\S+");
    private static final Pattern OUTPUT_PATTERN = Pattern.compile("-output +\\S+");

    /**
     * Создаёт ответ пользователю на основе его запроса.
     *
     * @param userName имя пользователя
     * @param text текст запроса
     * @return ответ пользователю
     */
    public Object execute(String userName, String text) {
        Matcher rateMatcher = RATE_PATTERN.matcher(text);
        Matcher dateMatcher = DATE_PATTERN.matcher(text);
        Matcher periodMatcher = PERIOD_PATTERN.matcher(text);
        Matcher algMatcher = ALG_PATTERN.matcher(text);
        Matcher outputMatcher = OUTPUT_PATTERN.matcher(text);

        if (!rateMatcher.find()) {
            log.debug("Пользователь {}. Не удалось в сообщении {} найти параметр rate", userName, text);
            return "Не вижу в вашем запросе параметра \"rate\". Повторите запрос или нажмите /help для помощи.";
        } else if (!dateMatcher.find()) {
            if (!periodMatcher.find()) {
                log.debug("Пользователь {}. Не удалось в сообщении {} найти параметр -date или -period", userName, text);
                return "Не вижу в вашем запросе параметра \"-date\" или \"-period\". Повторите запрос.";
            } else if (!outputMatcher.find()) {
                log.debug("Пользователь {}. Не удалось в сообщении {} найти параметр -output", userName, text);
                return "Не вижу в вашем запросе параметра \"-output\". Повторите запрос.";
            }
        }
        if (!algMatcher.find()) {
            log.debug("Пользователь {}. Не удалось в сообщении {} найти параметр -alg", userName, text);
            return "Не вижу в вашем запросе параметра \"-alg\". Повторите запрос.";
        }

        UserOptions userOptions = new UserOptions();
        Controller controller = new Controller(userOptions);

        String rate = rateMatcher.group().replaceFirst("[Rr]ate", "").trim();
        if (!userOptions.setRate(rate)) {
            log.debug("Пользователь {}. Параметр rate = {} не был принят.", userName, rate);
            return String.format("Не корректный ввод валюты - \"%s\". Повторите запрос.", rate);
        }

        if (dateMatcher.find(0)) {
            String date = dateMatcher.group().replace("-date", "").trim();
            if (!userOptions.setDate(date)) {
                log.debug("Пользователь {}. Параметр date = {} не был принят.", userName, date);
                return String.format("Не корректный ввод даты - \"%s\". Повторите запрос.", date);
            }
            if (userOptions.getDate().isBefore(LocalDate.now())) {
                log.debug("Пользователь {}. Параметр date = {} ссылается на прошедшие дни.", userName, date);
                return String.format("Прогноз на прошлое - \"%s\" не делаю. Посмотрите официальные источники)", date);
            } else if (userOptions.getDate().equals(LocalDate.now())) {
                log.debug("Пользователь {}. Параметр date = {} ссылается на сегодня.", userName, date);
                return String.format("\"%s\" - зачем делать прогноз на сегодня? Посмотрите официальные источники.",
                        date);
            }
        } else {
            String period = periodMatcher.group().replace("-period", "").trim();
            if (!userOptions.setCountDays(period)) {
                log.debug("Пользователь {}. Параметр period = {} не был принят", userName, period);
                return String.format("Не корректный ввод периода - \"%s\". Повторите запрос.", period);
            }

            String output = outputMatcher.group().replace("-output", "").trim();
            if (!userOptions.setView(output)) {
                log.debug("Пользователь {}. Параметр output = {} не был принят", userName, output);
                return String.format("Не корректный ввод способа вывода - \"%s\". Повторите запрос.", output);
            }
        }

        String alg = algMatcher.group().replace("-alg", "").trim();
        if (!userOptions.setAlgorithm(alg)) {
            log.debug("Пользователь {}. Параметр alg = {} не был принят", userName, alg);
            return String.format("Не корректный ввод алгоритма - \"%s\". Повторите запрос.", alg);
        }

        log.info("Пользователь {}. Запрос {} принят на обработку.", userName, text);
        return controller.makeCurrencyForecast();
    }
}
