package ru.liga.strategy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ArithmeticMeanForecastTest {
    private final Strategy strategy = new ArithmeticMeanForecast();
    private final Map<LocalDate, Double> forecastData = new TreeMap<>(Comparator.reverseOrder());

    @Before
    public void putElements() {
        for (int i = 0; i < 5; i++) {
            forecastData.put(LocalDate.now().plusDays(i), i + 1.5);
        }
    }

    @Test
    public void testGetForecast() {
        Map<LocalDate, Double> expected = new TreeMap<>();
        expected.put(LocalDate.now().plusDays(1), 3.5);

        Map<LocalDate, Double> actualFirst = strategy.getForecast(forecastData, ForecastPeriod.TOMORROW.getDayCount());
        Assert.assertEquals(expected, actualFirst);

        expected.put(LocalDate.now().plusDays(2), 3.9);
        expected.put(LocalDate.now().plusDays(3), 4.18);
        expected.put(LocalDate.now().plusDays(4), 4.316);
        expected.put(LocalDate.now().plusDays(5), 4.2792);
        expected.put(LocalDate.now().plusDays(6), 4.03504);
        expected.put(LocalDate.now().plusDays(7), 4.142048);

        Map<LocalDate, Double> actualSecond = strategy.getForecast(forecastData, ForecastPeriod.WEEK.getDayCount());
        Assert.assertEquals(expected, actualSecond);
    }
}