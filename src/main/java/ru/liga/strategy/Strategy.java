package ru.liga.strategy;

import java.time.LocalDate;
import java.util.Map;

/**
 * Объект стратегии, который содержит логику прогнозирования курса валют.
 */
public interface Strategy {

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
