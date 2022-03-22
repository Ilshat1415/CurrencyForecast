package ru.liga.algorithm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Алгоритм “Мистический”. Для расчета на дату использует среднее
 * арифметическое из трех последних от этой даты полнолуний.
 * Для расчета на неделю и месяц первый курс рассчитывается
 * аналогично предыдущему расчёту на день, а последующие даты рассчитываются
 * рекуррентно по формуле - значение предыдущей даты
 * + случайное число от -10% до +10% от значения предыдущей даты.
 */
public class MoonAlgorithm implements Algorithm {

    @Override
    public Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, LocalDate date, int period) {
        Map<LocalDate, Double> forecastData = new TreeMap<>();

        List<LocalDate> fullMoonDates = getListFullMoonDates(Collections.max(data.keySet()));

        double rate = (data.get(fullMoonDates.get(0))
                + data.get(fullMoonDates.get(1))
                + data.get(fullMoonDates.get(2))) / 3;

        forecastData.put(date, rate);

        for (int i = 1; i < period; i++) {
            date = date.plusDays(1);
            rate *= 0.9 + Math.random() * 0.2;

            forecastData.put(date, rate);
        }
        return forecastData;
    }

    /**
     * Формирует список дат полнолуний от 19.12.2021.
     *
     * @param lastDate последняя дата
     * @return список дат полнолуний
     */
    private List<LocalDate> getListFullMoonDates(LocalDate lastDate) {
        List<LocalDate> fullMoonDates = new ArrayList<>();
        LocalDateTime fullMoonDate = LocalDateTime
                .of(2021, 12, 19, 7, 38, 0);

        while (!fullMoonDate.isAfter(lastDate.atStartOfDay())) {
            fullMoonDates.add(fullMoonDate.toLocalDate());
            fullMoonDate = fullMoonDate
                    .plusDays(29)
                    .plusHours(12)
                    .plusMinutes(44)
                    .plusSeconds(3);
        }
        fullMoonDates.sort(Comparator.reverseOrder());
        return fullMoonDates;
    }
}
