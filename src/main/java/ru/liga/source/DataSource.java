package ru.liga.source;

import java.time.LocalDate;
import java.util.Map;

/**
 * Хранилище, которое содержит в себе начальные данные.
 */
public interface DataSource {

    /**
     * Возвращает данные о курсе валют в виде отсортированной по дате коллекции,
     * от большего к меньшему.
     *
     * @return Данные о курсе валюты
     */
    Map<LocalDate, Double> getData();
}
