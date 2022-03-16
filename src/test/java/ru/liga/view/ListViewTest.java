package ru.liga.view;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.liga.model.CurrencyName;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class ListViewTest {
    private final View view = new ListView();

    @Test
    public void testPreparingResults() {
        Map<LocalDate, Double> param = new TreeMap<>();
        param.put(LocalDate.of(2022, 3, 1), 25d);
        param.put(LocalDate.of(2022, 3, 2), 50d);

        view.preparingResults(CurrencyName.USD, param);

        String expected = "Прогноз для USD:\n" +
                "Вт 01.03.2022 - 25,00\n" +
                "Ср 02.03.2022 - 50,00\n\n";

        Assertions.assertThat(view.getAnswer()).isEqualTo(expected);
    }
}