package ru.liga.model;

import java.time.LocalDate;
import java.util.Map;

/**
 * Хранилище, которое содержит в себе начальные данные.
 */
public interface Storage {

    /**
     * Возвращает данные о курсе валют в виде отсортированной по дате коллекции,
     * от большего к меньшему.
     *
     * @param currencyName Название валюты
     * @return Данные о курсе валюты
     */
    Map<LocalDate, Double> getData(CurrencyName currencyName);
}
