package ru.liga.algorithm;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class RegressionAlgorithmTest {
    private final Algorithm algorithm = new RegressionAlgorithm();

    @Test
    public void testGetForecast() {
        Map<LocalDate, Double> param = new TreeMap<>(Comparator.reverseOrder());
        LocalDate date = LocalDate.now().minusDays(30);
        for (int i = 1; i <= 30; i++) {
            param.put(date.plusDays(i), i + 0d);
        }

        Map<LocalDate, Double> expected = new TreeMap<>();
        expected.put(date.plusDays(31), 31d);
        expected.put(date.plusDays(32), 32d);

        Assertions.assertThat(algorithm.getForecast(param, date.plusDays(31), 2)).isEqualTo(expected);
    }
}