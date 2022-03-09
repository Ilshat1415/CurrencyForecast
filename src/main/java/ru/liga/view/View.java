package ru.liga.view;

import java.time.LocalDate;
import java.util.Map;

/**
 * Объект, который отвечает за отображение пользователю результата работы программы,
 * а так же содержит методы работы с данным представлением.
 */
public interface View {

    /**
     * Вывод прогноз курса валют пользователю в удобном формате.
     *
     * @param forecastData Прогноз курса валюты
     */
    void displayForecast(Map<LocalDate, Double> forecastData);

    /**
     * Отображает пользователю сообщение.
     *
     * @param message Сообщение
     */
    void writeMessage(String message);

    /**
     * Считывает строку текста от пользователя.
     *
     * @return Строка
     */
    String readString();
}
