package ru.liga;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ru.liga.source.CurrencyName;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс отвечающий за хранение и выдачу результатов прогнозирования.
 */
@Slf4j
public class ForecastStorage {
    /**
     * Шаблон форматирования даты.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("E dd.MM.y");
    /**
     * Результаты прогнозирования.
     */
    private final Map<String, Map<LocalDate, Double>> data = new HashMap<>();

    /**
     * Кладёт результат прогнозирования в хранилище.
     *
     * @param currencyName название валюты
     * @param forecastData прогноз
     */
    public void putForecast(CurrencyName currencyName, Map<LocalDate, Double> forecastData) {
        data.put(currencyName.name(), forecastData);
    }

    /**
     * Возвращает список, отображающий результаты прогноза.
     *
     * @return список результата прогнозирования
     */
    public String getForecastList() {
        StringBuilder resultList = new StringBuilder();

        for (Map.Entry<String, Map<LocalDate, Double>> entry : data.entrySet()) {
            resultList.append("Прогноз для ").append(entry.getKey()).append(":\n");

            for (Map.Entry<LocalDate, Double> forecast : entry.getValue().entrySet()) {
                LocalDate key = forecast.getKey();
                Double value = forecast.getValue();

                resultList.append(key.format(DATE_TIME_FORMATTER))
                        .append(String.format(" - %.2f\n", value));
            }
            resultList.append("\n");
        }
        return resultList.toString();
    }

    /**
     * Возвращает график, отображающий результаты прогноза.
     *
     * @return график результатов прогнозирования
     */
    public JFreeChart getForecastGraph() {
        StringBuilder title = new StringBuilder("Прогноз для");
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();

        for (Map.Entry<String, Map<LocalDate, Double>> entry : data.entrySet()) {
            XYSeries series = new XYSeries(entry.getKey());

            int day = 0;
            for (Double rate : entry.getValue().values()) {
                series.add(++day, rate);
            }

            xySeriesCollection.addSeries(series);
            title.append(" ").append(entry.getKey());
        }

        JFreeChart chart = ChartFactory.createXYLineChart(title.toString(),
                "День", "Показатель, руб.",
                xySeriesCollection, PlotOrientation.VERTICAL,
                true, true, true);

        customizeChartDisplay(chart, xySeriesCollection);

        return chart;
    }

    /**
     * Настраивает линейный график.
     *
     * @param chart              график
     * @param xySeriesCollection данные
     */
    private void customizeChartDisplay(JFreeChart chart, XYSeriesCollection xySeriesCollection) {
        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setBorder(1, 1, 1, 1);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setUseFillPaint(true);
        plot.setRenderer(renderer);

        plot.getRangeAxis().setRange(xySeriesCollection.getRangeLowerBound(false) * 0.95,
                xySeriesCollection.getRangeUpperBound(false) * 1.05);
        plot.getDomainAxis().setRange(1.0, xySeriesCollection.getDomainUpperBound(false));

    }
}
