package ru.liga.view;

import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ru.liga.model.CurrencyName;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Графическое представление результатов прогнозирования.
 */
@Slf4j
public class GraphView implements View {
    /**
     * Название рисунка.
     */
    private final StringBuilder title = new StringBuilder("Прогноз курса для");
    /**
     * Коллекция графиков.
     */
    private final XYSeriesCollection seriesCollection = new XYSeriesCollection();

    @Override
    public void preparingResults(CurrencyName currencyName, Map<LocalDate, Double> forecastData) {
        XYSeries series = new XYSeries(currencyName);
        int day = 0;
        for (Double rate : forecastData.values()) {
            series.add(++day, rate);
        }
        seriesCollection.addSeries(series);
        title.append(" ").append(currencyName);
    }

    @Override
    public Object getAnswer() {
        try {
            RenderedImage image = getGraph().createBufferedImage(800, 700);
            File file = File.createTempFile("image", ".tmp");
            ImageIO.write(image, "png", file);
            return file;
        } catch (IOException e) {
            log.error("Ошибка {}. Создание временного файла с изображением.", e.getMessage());
            return "Не удалось создать график";
        }
    }

    /**
     * Создает и настраивает линейный график.
     *
     * @return линейный график
     */
    private JFreeChart getGraph() {
        JFreeChart chart = ChartFactory.createXYLineChart(title.toString(), "День", "Показатель, руб.",
                seriesCollection, PlotOrientation.VERTICAL,
                true, true, true);

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setUseFillPaint(true);
        plot.setRenderer(renderer);

        plot.getRangeAxis().setRange(getMinY(), getMaxY());
        plot.getDomainAxis().setRange(1.0, getMaxX());

        plot.setRangeGridlinePaint(Color.BLACK);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setBorder(1, 1, 1, 1);

        return chart;
    }

    /**
     * Возвращает максимальное значение по оси X.
     *
     * @return максимальное значение X
     */
    private double getMaxX() {
        return seriesCollection.getDomainUpperBound(false);
    }

    /**
     * Возвращает минимальное значение по оси Y.
     *
     * @return минимальное значение Y
     */
    private double getMinY() {
        return seriesCollection.getRangeLowerBound(false) * 0.95;
    }

    /**
     * Возвращает максимальное значение по оси Y.
     *
     * @return максимальное значение Y
     */
    private double getMaxY() {
        return seriesCollection.getRangeUpperBound(false) * 1.05;
    }
}
