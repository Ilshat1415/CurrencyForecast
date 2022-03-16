package ru.liga.algorithm;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * Алгоритм линейной регресии, который использует готовую логику линейной регресии,
 * экстраполируя курс по данным последнего месяца.
 */
public class RegressionAlgorithm implements Algorithm {
    /**
     * Количество используемых данных.
     */
    private static final int amountDataUse = 30;

    @Override
    public Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, LocalDate date, int period) {
        Double[] rates = data.values().stream().limit(amountDataUse).sorted().toArray(Double[]::new);
        Double[] days = new Double[amountDataUse];
        for (int i = 0; i < amountDataUse; i++) {
            days[i] = i + 1.0;
        }
        LinearRegression linearRegression = new LinearRegression(days, rates);

        Map<LocalDate, Double> forecastData = new TreeMap<>();
        int countDay = date.getDayOfYear() - LocalDate.now().getDayOfYear() + 365 * (date.getYear() - LocalDate.now().getYear());
        for (int i = countDay; i < countDay + period; i++) {
            Double rate = linearRegression.predict(30 + i);
            forecastData.put(date, rate);
            date = date.plusDays(1);
        }
        return forecastData;
    }
}
