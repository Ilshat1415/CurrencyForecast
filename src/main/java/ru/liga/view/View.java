package ru.liga.view;

import ru.liga.model.CurrencyName;

import java.time.LocalDate;
import java.util.Map;

/**
 * Класс, который отвечает за вид результата прогноза, который будут представлен пользователю.
 */
public interface View {

    /**
     * Подготовка результатов прогноза к отображению пользователю.
     *
     * @param currencyName название валюты
     * @param forecastData прогноз курса валюты
     */
    void preparingResults(CurrencyName currencyName, Map<LocalDate, Double> forecastData);

    /**
     * Возвращает результат прогноза в необходимом представлении.
     *
     * @return результат прогноза
     */
    Object getAnswer();
}
