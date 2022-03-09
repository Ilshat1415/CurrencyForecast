package ru.liga.strategy;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Стратегия прогнозирования курса валют
 * путем получения среднеарифметического значения
 * на основании 7 последних курсов.
 */
public class ArithmeticMeanForecast implements Strategy {
    /**
     * Количество используемых данных.
     */
    private static final int amountDataUse = 7;

    @Override
    public Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, int period) {
        Map<LocalDate, Double> forecastData = new TreeMap<>();

        List<Double> courses = data.values()
                .stream()
                .limit(amountDataUse)
                .collect(Collectors.toList());

        for (int i = 1; i <= period; i++) {
            Double averageRate = courses.stream()
                    .mapToDouble(d -> d)
                    .average()
                    .getAsDouble();

            forecastData.put(LocalDate.now().plusDays(i), averageRate);

            Collections.rotate(courses, 1);
            courses.set(0, averageRate);
        }
        return forecastData;
    }
}
