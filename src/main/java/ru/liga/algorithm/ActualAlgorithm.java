package ru.liga.algorithm;

import ru.liga.exceptions.NotFoundDataException;

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
    public Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, LocalDate date, int period) throws NotFoundDataException {
        Map<LocalDate, Double> forecastData = new TreeMap<>();

        for (int i = 0; i < period; i++) {
            Double dateTwoYearsAgo = data.get(date.minusYears(2));
            Double dateThreeYearsAgo = data.get(date.minusYears(3));

            if (dateTwoYearsAgo == null || dateThreeYearsAgo == null) {
                throw new NotFoundDataException("К сожалению алгоритм \"Актуальный\" не умеет делать прогноз на такое далёкое будущее.");
            }

            Double rate = dateTwoYearsAgo + dateThreeYearsAgo;

            forecastData.put(date, rate);
            date = date.plusDays(1);
        }
        return forecastData;
    }
}
