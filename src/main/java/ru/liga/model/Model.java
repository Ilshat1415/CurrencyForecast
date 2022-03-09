package ru.liga.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * Объект, который содержит в себе логику считывания данных.
 */
public interface Model {

    /**
     * Возвращает данные о курсе валют в виде отсортированной по дате коллекции,
     * от большего к меньшему.
     *
     * @param currencyName Название валюты
     * @return Данные о курсе валюты
     */
    Map<LocalDate, Double> getData(CurrencyNames currencyName);
}
