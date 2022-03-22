package ru.liga.source;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Класс исходного файла, отвечающий за считывание и выдачу данных.
 */
@Slf4j
public class FileDataSource implements DataSource {
    /**
     * Путь к локальному файлу.
     */
    private static final String FILE_PATH = "/%s_F01_02_2005_T05_03_2022.csv";
    /**
     * Исходные данные о валюте.
     */
    private final Map<LocalDate, Double> data = new TreeMap<>(Comparator.reverseOrder());

    public FileDataSource(CurrencyName currencyName) {
        try (InputStream in = getClass().getResourceAsStream(String.format(FILE_PATH, currencyName.name()));
             BufferedReader readFile = new BufferedReader(new InputStreamReader(in))) {
            readFile.readLine();

            String line;
            while ((line = readFile.readLine()) != null) {
                fillData(line.split(";"));
            }
        } catch (IOException e) {
            log.error("Ошибка {}. Чтении данных из файлов.", e.getMessage(), e);
        }
    }

    @Override
    public Map<LocalDate, Double> getData() {
        return data;
    }

    /**
     * Заполняет данные из массива.
     *
     * @param dateOneDay Данные одного дня
     */
    private void fillData(String[] dateOneDay) {
        int nominal = Integer.parseInt(dateOneDay[0].replaceAll("\\.", ""));
        LocalDate date = LocalDate.parse(dateOneDay[1], DateTimeFormatter.ofPattern("dd.MM.y"));
        double rate = Double.parseDouble(dateOneDay[2].replace(",", ".")
                .replaceAll("\"", ""));

        do {
            data.put(date, rate / nominal);
            if (data.size() == 1) {
                break;
            }
            date = date.plusDays(1);
        } while (!data.containsKey(date));
    }
}
