package ru.liga.view;

import ru.liga.model.CurrencyName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Представление результатов прогноза в виде списка.
 */
public class ListView implements View {
    /**
     * Шаблон форматирования даты.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("E dd.MM.y");
    /**
     * Ответ для пользоввателя.
     */
    private final StringBuilder answer = new StringBuilder();

    @Override
    public void preparingResults(CurrencyName currencyName, Map<LocalDate, Double> forecastData) {
        answer.append("Прогноз для ").append(currencyName).append(":\n");
        for (Map.Entry<LocalDate, Double> entry : forecastData.entrySet()) {
            LocalDate key = entry.getKey();
            Double value = entry.getValue();
            answer.append(key.format(DATE_TIME_FORMATTER))
                    .append(String.format(" - %.2f\n", value));
        }
        answer.append("\n");
    }

    @Override
    public Object getAnswer() {
        return answer.toString();
    }
}
