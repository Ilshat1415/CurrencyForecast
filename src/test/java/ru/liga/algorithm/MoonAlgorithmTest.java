package ru.liga.algorithm;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MoonAlgorithmTest{
    private final Algorithm algorithm = new MoonAlgorithm();

    @Test
    public void testGetForecast() {
        Map<LocalDate, Double> param = new HashMap<>();
        for (int i = 0; i < 120; i++) {
            param.put(LocalDate.now().minusDays(i), 100d);
        }

        Map<LocalDate, Double> expected = new HashMap<>();
        LocalDate date = LocalDate.now().plusDays(1);
        expected.put(date, 100d);

        Assertions.assertThat(algorithm.getForecast(param, date, 1)).isEqualTo(expected);
    }
}