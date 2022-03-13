package ru.liga.strategy;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

/**
 * Стратегия прогнозирования курса валют
 * путем получения среднеарифметического значения
 * на основании 7 последних курсов.
 */
public class AlgorithmLinearRegression implements Strategy {
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
            date = date.plusDays(1);
            Double rate = linearRegression.predict(30 + i);
            forecastData.put(date, rate);
        }
        return forecastData;
    }
}
