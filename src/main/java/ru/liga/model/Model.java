package ru.liga.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Объект, который содержит в себе логику считывания данных
 * и использование стратегий прогнозирования курса валют.
 */
public interface Model {

    /**
     * Считывает данные о курсе валюты из локального файла,
     * возвращая их в виде отсортированной по дате коллекции.
     *
     * @param currencyName Название валюты
     * @return Данные о курсе валюты
     * @throws IOException          Если проблемы с чтением файлов
     * @throws NullPointerException Если валюта не найдена
     */
    Map<LocalDate, Double> getData(String currencyName) throws IOException, NullPointerException;
}
