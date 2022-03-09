package ru.liga.strategy;

import java.time.LocalDate;
import java.util.*;

/**
 * Стратегия прогнозирования курса валют
 * путем получения среднеарифметического значения
 * на основании 7 последних курсов.
 */
public class ArithmeticMeanForecast implements Strategy {

    @Override
    public Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, String period) throws IllegalArgumentException {
        Map<LocalDate, Double> forecastData = new TreeMap<>();

        List<Double> courses = new ArrayList<>(data.values());
        for (int i = 1; i <= getPeriod(period); i++) {
            Double averageRate = courses.stream()
                    .mapToDouble(d -> d)
                    .average()
                    .getAsDouble();

            forecastData.put(LocalDate.now().plusDays(i), averageRate);

            courses.set(0, averageRate);
            Collections.rotate(courses, -1);
        }
        return forecastData;
    }

    /**
     * Конвертирует строковое название периода прогнозирования в целочисленное значение.
     *
     * @param period Период прогнозирования
     * @return Количество дней для прогноза
     * @throws IllegalArgumentException Если период не поддерживается
     */
    private int getPeriod(String period) throws IllegalArgumentException {
        if ("tomorrow".equals(period)) {
            return 1;
        } else if ("week".equals(period)) {
            return 7;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
