package ru.liga.telegram;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.liga.algorithm.Algorithm;
import ru.liga.algorithm.AlgorithmName;
import ru.liga.algorithm.ForecastPeriod;
import ru.liga.source.CurrencyName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс хранящий запрос пользователя.
 */
@Slf4j
@Getter
public class TelegramRateRequest {
    /**
     * Список валют.
     */
    private final List<CurrencyName> currencyNames;
    /**
     * Дата прогноза.
     */
    private final LocalDate date;
    /**
     * Количество дней для прогноза.
     */
    private final int countDays;
    /**
     * Алгоритм прогнозирования.
     */
    private final Algorithm algorithm;
    /**
     * Способ вывода результата.
     */
    private final String output;

    /**
     * Создает объект запроса из текстовой строки, если запрос корректный.
     *
     * @param text текст запроса
     * @throws IllegalArgumentException если текст запроса не корректен
     */
    public TelegramRateRequest(String text) throws IllegalArgumentException {
        String[] userRequests = text.split(" ");
        if (text.matches("[Rr]ate \\S+ -date \\S+ -alg \\S+")) {
            this.date = parseDate(userRequests[3]);
            if (!date.isAfter(LocalDate.now())) {
                throw new IllegalArgumentException("Прозноз не доступен на дату раньше, чем завтра.");
            }
            this.countDays = 1;
            this.output = "list";
        } else if (text.matches("[Rr]ate \\S+ -period \\S+ -alg \\S+ -output \\S+")) {
            this.date = LocalDate.now().plusDays(1);
            this.countDays = parseCountDays(userRequests[3]);
            if (isOutput(userRequests[7])) {
                this.output = userRequests[7].toLowerCase();
            } else {
                throw new IllegalArgumentException(String
                        .format("Не корректный способ вывода - \"%s\". Повторите запрос.", userRequests[7]));
            }
        } else {
            throw new IllegalArgumentException("Запрос введён не по шаблону," +
                    "пожалуйста проверьте свой запрос.\n" +
                    "Чтобы посмотреть шаблоны запроса нажмите /help.");
        }
        this.currencyNames = parseRates(userRequests[1]);
        this.algorithm = parseAlgorithm(userRequests[5]);
    }

    /**
     * Получает список экземпляров CurrencyName из текстовой строки.
     *
     * @param rate название валют
     * @return список валют
     * @IllegalArgumentException если строка не содержит названия валют
     */
    private List<CurrencyName> parseRates(String rate) throws IllegalArgumentException {
        try {
            ArrayList<CurrencyName> currencyNames = new ArrayList<>();
            if (rate.contains(",")) {
                for (String r : rate.split(",")) {
                    currencyNames.add(CurrencyName.valueOf(r.toUpperCase()));
                }
            } else {
                currencyNames.add(CurrencyName.valueOf(rate.toUpperCase()));
            }
            return currencyNames;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String
                    .format("Не корректный ввод валюты - \"%s\". Повторите запрос.", rate));
        }
    }

    /**
     * Получает экземпляр LocalDate из текстовой строки.
     *
     * @param date дата в текстовом виде
     * @return дата
     * @IllegalArgumentException если строка не содержит даты
     */
    private LocalDate parseDate(String date) throws IllegalArgumentException {
        try {
            if ("tomorrow".equalsIgnoreCase(date)) {
                return LocalDate.now().plusDays(1);
            } else {
                return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.y"));
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(String
                    .format("Не корректный ввод даты - \"%s\". Повторите запрос.", date));
        }
    }

    /**
     * Получает экземпляров Algorithm из текстовой строки.
     *
     * @param alg название алгоритма
     * @return алгоритм
     * @IllegalArgumentException если строка не содержит названия алгоритма
     */
    private Algorithm parseAlgorithm(String alg) throws IllegalArgumentException {
        try {
            return AlgorithmName.valueOf(alg.toUpperCase()).getAlgorithm();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String
                    .format("Не корректный ввод алгоритма - \"%s\". Повторите запрос.", alg));
        }
    }

    /**
     * Получает количество дней из текстового периода.
     *
     * @param period период
     * @return количество дней
     * @IllegalArgumentException если строка не содержит названия периода
     */
    private int parseCountDays(String period) throws IllegalArgumentException {
        try {
            return ForecastPeriod.valueOf(period.toUpperCase()).getDayCount();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String
                    .format("Не корректный ввод периода - \"%s\". Повторите запрос.", period));
        }
    }

    /**
     * Проверяет поддерживается ли способ вывода.
     *
     * @param output название способа вывода
     * @return true если способ вывода поддерживается, иначе false
     */
    private boolean isOutput(String output) {
        return "list".equalsIgnoreCase(output) || "graph".equalsIgnoreCase(output);
    }
}
