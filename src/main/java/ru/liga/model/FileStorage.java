package ru.liga.model;

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
 * Файловое хранилище, которое считывает данные из локальных файлов.
 */
@Slf4j
public class FileStorage implements Storage {

    /**
     * Синглтон.
     */
    private static class SingletonHolder {
        public static final FileStorage HOLDER_INSTANCE = new FileStorage();
    }

    public static FileStorage getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

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
     * Создание объекта файлового хранилища, которое сразу считывает и сохраняет в удобном формате данные из файлов.
     */
    private FileStorage() {
        for (CurrencyName currencyName : CurrencyName.values()) {
            try (InputStream inputStream = getClass().getResourceAsStream(String.format(FILE_PATH, currencyName.name()));
                 BufferedReader readFile = new BufferedReader(new InputStreamReader(inputStream))) {
                readFile.readLine();
                while (readFile.ready()) {
                    String[] dateOneDay = readFile.readLine().split(";");
                    convertDataAndPut(dateOneDay, currencyName);
                }
            } catch (IOException e) {
                log.error("Ошибка {}. Чтении данных из файлов.", e.getMessage(), e);
            } catch (NullPointerException e) {
                log.error("Не было найдено файла с данными для {}", currencyName.name(), e);
            }
        }
        log.info("Данные успешно считаны.");
    }

    @Override
    public Map<LocalDate, Double> getData(CurrencyName currencyName) {
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
     * @param dateOneDay   Данные одного дня
     * @param currencyName Название валюты
     */
    private void convertDataAndPut(String[] dateOneDay, CurrencyName currencyName) {
        int nominal = Integer.parseInt(dateOneDay[0].replaceAll("\\.", ""));
        LocalDate rateDate = LocalDate.parse(dateOneDay[1], DateTimeFormatter.ofPattern("dd.MM.y"));
        Double rate = Double.parseDouble(dateOneDay[2].replace(",", ".")
                .replaceAll("\"", "")) / nominal;

        do {
            getData(currencyName).put(rateDate, rate);
            if (getData(currencyName).size() == 1) {
                break;
            }
            rateDate = rateDate.plusDays(1);
        } while (!getData(currencyName).containsKey(rateDate));
    }
}
