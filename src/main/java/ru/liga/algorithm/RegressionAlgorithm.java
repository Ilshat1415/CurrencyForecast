package ru.liga.algorithm;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Алгоритм линейной регресии, который использует готовую логику линейной регресии,
 * экстраполируя курс по данным последнего месяца.
 */
public class RegressionAlgorithm implements Algorithm {
    /**
     * Количество используемых данных.
     */
    private static final int amountDataUse = 30;
    private double intercept, slope;

    @Override
    public Map<LocalDate, Double> getForecast(Map<LocalDate, Double> data, LocalDate date, int period) {
        Double[] days = Stream.iterate(30d, n -> n - 1d)
                .limit(amountDataUse)
                .toArray(Double[]::new);
        Double[] rates = data.values()
                .stream()
                .limit(amountDataUse)
                .toArray(Double[]::new);

        createLinearRegression(days, rates);

        Map<LocalDate, Double> forecastData = new TreeMap<>();
        int countDay = date.getDayOfYear() - LocalDate.now().getDayOfYear() + 365 * (date.getYear() - LocalDate.now().getYear());
        for (int i = countDay; i < countDay + period; i++) {
            Double rate = predict(30 + i);
            forecastData.put(date, rate);
            date = date.plusDays(1);
        }
        return forecastData;
    }

    /**
     * Создает линейную регрессию.
     *
     * @param days  дни
     * @param rates курс
     */
    private void createLinearRegression(Double[] days, Double[] rates) {
        int n = days.length;

        double sumX = 0.0, sumY = 0.0;
        for (int i = 0; i < n; i++) {
            sumX += days[i];
            sumY += rates[i];
        }
        double xBar = sumX / n;
        double yBar = sumY / n;

        double xXBar = 0.0, xYBar = 0.0;
        for (int i = 0; i < n; i++) {
            xXBar += (days[i] - xBar) * (days[i] - xBar);
            xYBar += (days[i] - xBar) * (rates[i] - yBar);
        }
        slope = xYBar / xXBar;
        intercept = yBar - slope * xBar;
    }

    /**
     * Делает прогноз значения по регрессии.
     *
     * @param day номер дня
     * @return прогноз
     */
    private double predict(double day) {
        return slope * day + intercept;
    }
}
