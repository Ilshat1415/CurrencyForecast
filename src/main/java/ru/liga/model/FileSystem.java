package ru.liga.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Обект файловой системы, который считывает данные из локальных файлов.
 */
public class FileSystem implements Model {
    /**
     * Путь к локальному файлу.
     */
    private static final String FILE_PATH = "/%s_F01_02_2005_T05_03_2022.csv";
    /**
     * Курс валюты "Армянский драм"
     */
    private static final Map<LocalDate, Double> AMD_DATE = new TreeMap<>(Comparator.reverseOrder());
    /**
     * Курс валюты "Болгарский лев"
     */
    private static final Map<LocalDate, Double> BGN_DATE = new TreeMap<>(Comparator.reverseOrder());
    /**
     * Курс валюты "Евро"
     */
    private static final Map<LocalDate, Double> EUR_DATE = new TreeMap<>(Comparator.reverseOrder());
    /**
     * Курс валюты "Турецкая лира"
     */
    private static final Map<LocalDate, Double> TRY_DATE = new TreeMap<>(Comparator.reverseOrder());
    /**
     * Курс валюты "Доллар США"
     */
    private static final Map<LocalDate, Double> USD_DATE = new TreeMap<>(Comparator.reverseOrder());

    /**
     * Создание объекта файловой системы, считывает и сохраняет в удобном формате данные из файлов.
     */
    public FileSystem() {
        Logger log = LoggerFactory.getLogger(FileSystem.class);

        for (CurrencyNames currencyName : CurrencyNames.values()) {
            try (InputStream inputStream = getClass().getResourceAsStream(String.format(FILE_PATH, currencyName.name()));
                 BufferedReader readFile = new BufferedReader(new InputStreamReader(inputStream, "windows-1251"))) {
                readFile.readLine();
                while (readFile.ready()) {
                    String[] dateOneDay = readFile.readLine().split(";");
                    convertDataAndPutMap(dateOneDay);
                }
            } catch (IOException e) {
                log.error("Ошибка при чтении данных из файлов", e);
            } catch (NullPointerException e) {
                log.info(String.format("Не было найдено файла с данными для %s", currencyName.name()));
            }
        }
    }

    @Override
    public Map<LocalDate, Double> getData(CurrencyNames currencyName) {
        switch (currencyName) {
            case AMD:
                return AMD_DATE;
            case BGN:
                return BGN_DATE;
            case EUR:
                return EUR_DATE;
            case TRY:
                return TRY_DATE;
            case USD:
                return USD_DATE;
            default:
                return null;
        }
    }

    /**
     * Конвертирует данные из масива для хранения в мапе.
     *
     * @param dateOneDay Данные одного дня
     */
    private void convertDataAndPutMap(String[] dateOneDay) {
        int nominal = Integer.parseInt(dateOneDay[0].replaceAll("\\.", ""));
        LocalDate rateDate = LocalDate.parse(dateOneDay[1], DateTimeFormatter.ofPattern("dd.MM.y"));
        Double rate = Double.parseDouble(dateOneDay[2].replace(",", ".")
                .replaceAll("\"", ""))
                / nominal;
        switch (dateOneDay[3]) {
            case "Армянский драм":
                AMD_DATE.put(rateDate, rate);
            case "Болгарский лев":
                BGN_DATE.put(rateDate, rate);
            case "Евро":
                EUR_DATE.put(rateDate, rate);
            case "Турецкая лира":
                TRY_DATE.put(rateDate, rate);
            case "Доллар США":
                USD_DATE.put(rateDate, rate);
        }
    }
}
