package ru.liga.strategy;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class AlgorithmActual implements Strategy {

    @Override
    public Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, LocalDate date, int period) {
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
