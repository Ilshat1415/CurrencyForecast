package ru.liga.algorithm;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class ActualAlgorithmTest {
    private final Algorithm algorithm = new ActualAlgorithm();

    @Test
    public void testGetForecast() {
        Map<LocalDate, Double> param = new TreeMap<>();
        param.put(LocalDate.of(2019, 1, 1), 50d);
        param.put(LocalDate.of(2020, 1, 1), 25d);
        param.put(LocalDate.of(2019, 1, 2), 75d);
        param.put(LocalDate.of(2020, 1, 2), 50d);

        Map<LocalDate, Double> expected = new TreeMap<>();
        LocalDate date = LocalDate.of(2022, 1, 1);
        expected.put(date, 75d);
        expected.put(date.plusDays(1), 125d);

        Assertions.assertThat(algorithm.getForecast(param, date, 2)).isEqualTo(expected);
    }
}