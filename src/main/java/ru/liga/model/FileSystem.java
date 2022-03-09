package ru.liga.model;

import ru.liga.strategy.Strategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

/**
 * Обект файловой системы, который считывает данные из локальных файлов.
 * Имеет возможность использовать разную стратегию прогнозирования курса валют.
 */
public class FileSystem implements Model {
    /**
     * Путь к локальному файлу.
     */
    private static final String FILE_PATH = "/%s_F01_02_2002_T01_02_2022.csv";
    /**
     * Количество данных.
     */
    private static final int amountDataUse = 7;

    @Override
    public Map<LocalDate, Double> getData(String currencyName) throws IOException, NullPointerException {
        Map<LocalDate, Double> data = new TreeMap<>();

        try (InputStream inputStream = getClass().getResourceAsStream(String.format(FILE_PATH, currencyName));
             BufferedReader readFile = new BufferedReader(new InputStreamReader(inputStream))) {
            readFile.readLine();
            for (int i = 0; i < amountDataUse; i++) {
                String[] dateOneDay = readFile.readLine().split(";");
                LocalDate rateDate = LocalDate.parse(dateOneDay[0], DateTimeFormatter.ofPattern("dd.MM.y"));
                Double rate = Double.parseDouble(dateOneDay[1].replace(",", "."));
                data.put(rateDate, rate);
            }
        }
        return data;
    }
}
