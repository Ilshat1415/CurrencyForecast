package ru.liga.algorithm;

import java.time.LocalDate;
import java.util.Map;

/**
 * Алгоритм, который содержит логику прогнозирования курса валют.
 */
public interface Algorithm {

    /**
     * Совершает прогноз курса валюты на заданный период,
     * возвращая отсортированную по дате коллекцию.
     *
     * @param data   Данные о курсе валюты
     * @param date   Дата прогноза
     * @param period Период прогнозирования
     * @return Прогноз курса валюты на заданный период
     */
    Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, LocalDate date, int period);
}
