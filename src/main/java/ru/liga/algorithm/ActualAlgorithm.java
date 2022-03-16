package ru.liga.algorithm;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * Алгоритм “Актуальный”. Рассчитывается, как сумма курса за
 * (текущий год - 2 + текущий год - 3). Если число сильно впереди
 * кидает ошибку.
 */
public class ActualAlgorithm implements Algorithm {

    /**
     * @throws NullPointerException если дата сильно впереди.
     */
    @Override
    public Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, LocalDate date, int period) throws NullPointerException {
        Map<LocalDate, Double> forecastData = new TreeMap<>();

        for (int i = 0; i < period; i++) {
            Double averageRate = data.get(date.minusYears(2))
                    + data.get(date.minusYears(3));

            forecastData.put(date, averageRate);
            date = date.plusDays(1);
        }
        return forecastData;
    }
}
