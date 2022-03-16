package ru.liga.telegram;

import lombok.Getter;
import ru.liga.algorithm.Algorithm;
import ru.liga.algorithm.AlgorithmName;
import ru.liga.algorithm.ForecastPeriod;
import ru.liga.model.CurrencyName;
import ru.liga.view.GraphView;
import ru.liga.view.ListView;
import ru.liga.view.View;
import ru.liga.view.ViewName;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс хранящий параметры указанные пользователем.
 */
@Getter
public class UserOptions {
    /**
     * Список валют.
     */
    private List<CurrencyName> currencyNames;
    /**
     * Дата прогноза.
     */
    private LocalDate date = LocalDate.now().plusDays(1);
    /**
     * Способ вывода результата.
     */
    private View view = new ListView();
    /**
     * Количество дней для прогноза.
     */
    private int countDays = 1;
    /**
     * Алгоритм прогнозирования.
     */
    private Algorithm algorithm;

    /**
     * Заполняет список валютой, которую указал пользователь.
     *
     * @param rate название валюты
     * @return true если название соответвует поддерживаемой валюте, иначе false
     */
    public boolean setRate(String rate) {
        try {
            ArrayList<CurrencyName> currencyNames = new ArrayList<>();
            if (rate.contains(",")) {
                for (String r : rate.split(",")) {
                    currencyNames.add(CurrencyName.valueOf(r.toUpperCase()));
                }
            } else {
                currencyNames.add(CurrencyName.valueOf(rate.toUpperCase()));
            }
            this.currencyNames = currencyNames;
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Устанавливает дату, которую указал пользователь.
     *
     * @param date дата прогноза
     * @return true если дата имеет правильный формат, иначе false
     */
    public boolean setDate(String date) {
        try {
            LocalDate localDate;
            if ("tomorrow".equalsIgnoreCase(date)) {
                localDate = LocalDate.now().plusDays(1);
            } else {
                localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.y"));
            }
            this.date = localDate;
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Устанавливает способ вывода результата.
     *
     * @param output способ вывода
     * @return true если возможно установить данное представление, иначе false
     */
    public boolean setView(String output) {
        try {
            if (ViewName.valueOf(output.toUpperCase()) == ViewName.GRAPH) {
                this.view = new GraphView();
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Устанавливает алгоритм прогнозирования, который запрашивает пользователь.
     *
     * @param alg название алгоритма
     * @return true если такой алгоритм существует, иначе false
     */
    public boolean setAlgorithm(String alg) {
        try {
            this.algorithm = AlgorithmName.valueOf(alg.toUpperCase()).getAlgorithm();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Устанавливает период пронозивания.
     *
     * @param period период
     * @return true если указанный период поддерживается, иначе false
     */
    public boolean setCountDays(String period) {
        try {
            this.countDays = ForecastPeriod.valueOf(period.toUpperCase()).getDayCount();
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
