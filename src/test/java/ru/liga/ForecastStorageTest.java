package ru.liga;


import org.assertj.core.api.Assertions;
import org.jfree.chart.JFreeChart;
import org.junit.Before;
import org.junit.Test;
import ru.liga.source.CurrencyName;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class ForecastStorageTest {
    private final ForecastStorage forecastStorage = new ForecastStorage();

    @Before
    public void putData() {
        Map<LocalDate, Double> dataFirst = new TreeMap<>();
        dataFirst.put(LocalDate.of(2020, 1, 1), 100d);
        dataFirst.put(LocalDate.of(2020, 1, 2), 200d);


        forecastStorage.putForecast(CurrencyName.USD, dataFirst);

        Map<LocalDate, Double> dataSecond = new TreeMap<>();
        dataSecond.put(LocalDate.of(2020, 1, 1), 150d);
        dataSecond.put(LocalDate.of(2020, 1, 2), 300d);

        forecastStorage.putForecast(CurrencyName.AMD, dataSecond);
    }

    @Test
    public void testGetForecastList() {
        String expected = "Прогноз для USD:\n" +
                "Ср 01.01.2020 - 100,00\n" +
                "Чт 02.01.2020 - 200,00\n\n" +
                "Прогноз для AMD:\n" +
                "Ср 01.01.2020 - 150,00\n" +
                "Чт 02.01.2020 - 300,00\n\n";
        Assertions.assertThat(forecastStorage.getForecastList()).isEqualTo(expected);
    }

    @Test
    public void testGetForecastGraph() {
        JFreeChart chart = forecastStorage.getForecastGraph();
        Assertions.assertThat(chart.getTitle().getText()).isEqualTo("Прогноз для USD AMD");
        Assertions.assertThat(chart.getXYPlot().getRangeAxis().getLowerBound()).isEqualTo(95);
        Assertions.assertThat(chart.getXYPlot().getRangeAxis().getUpperBound()).isEqualTo(315);
        Assertions.assertThat(chart.getXYPlot().getDomainAxis().getLowerBound()).isEqualTo(1);
        Assertions.assertThat(chart.getXYPlot().getDomainAxis().getUpperBound()).isEqualTo(2);
    }
}