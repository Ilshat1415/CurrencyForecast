package ru.liga.strategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class AlgorithmMoon implements Strategy {
    private final List<LocalDate> fullMoonDates = new ArrayList<>();

    {
        LocalDateTime fullMoonDate = LocalDateTime.of(2021, 12, 19, 7, 38, 0);
        while (fullMoonDate.isBefore(LocalDateTime.now())) {
            fullMoonDates.add(fullMoonDate.toLocalDate());
            fullMoonDate = fullMoonDate.plusDays(29)
                    .plusHours(12)
                    .plusMinutes(44)
                    .plusSeconds(3);
        }
        fullMoonDates.sort(Comparator.reverseOrder());
    }

    @Override
    public Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, LocalDate date, int period) {
        Map<LocalDate, Double> forecastData = new TreeMap<>();
        double rate = (data.get(fullMoonDates.get(0)) + data.get(fullMoonDates.get(1)) + data.get(fullMoonDates.get(2))) / 3;
        forecastData.put(date, rate);
        for (int i = 1; i < period; i++) {
            date = date.plusDays(1);
            rate = rate + (Math.random() * 0.2 * rate - 0.1 * rate);

            forecastData.put(date, rate);
        }
        return forecastData;
    }
}
