package ru.liga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.model.CurrencyNames;
import ru.liga.model.Model;
import ru.liga.strategy.AlgorithmNames;
import ru.liga.strategy.ForecastPeriod;
import ru.liga.strategy.Strategy;
import ru.liga.view.View;
import ru.liga.view.Views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Объект контроллера по прогнозу курса валют, который считывает запросы пользователя.
 * Получает данные прогноза от модели, в зависимости от переданного запроса,
 * и передаёт их для дальнейшего отображения результата пользователю.
 */
public class Controller {
    private final Logger log = LoggerFactory.getLogger(Controller.class);
    /**
     * Модель.
     */
    private final Model model;
    /**
     * Вид программы.
     */
    private final View view;
    private CurrencyNames currencyName;
    private LocalDate date;
    private int countDays;
    private AlgorithmNames alg;
    private Views output;

    /**
     * Создает контроллер, устанавливая логику работы программы и её вид.
     *
     * @param model Модель
     * @param view  Вид программы
     */
    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Осуществляет прогноз курса валюты.
     */
    public void makeCurrencyForecast() {
        try {
            getUserRequests();
            Strategy strategy = alg.getStrategy();
            Map<LocalDate, Double> data = model.getData(currencyName);
            Map<LocalDate, Double> forecast;
            forecast = strategy.getForecast(data, date, countDays);
            view.displayForecast(forecast);
        } catch (NullPointerException ignored) {
        }
    }

    /**
     *
     */
    private void getUserRequests() {
        String userRequest = view.readString();
        if ("exit".equals(userRequest)) {
            throw new RuntimeException();
        }

        String[] userRequests = userRequest.split(" ");
        if (userRequest.matches("rate .+ -date .+ -alg .+")) {
            setCurrencyName(userRequests[1]);
            setDate(userRequests[3]);
            setAlgorithm(userRequests[5]);
        } else if (userRequest.matches("rate .+ -period .+ -alg .+ -output .+")) {
            setCurrencyName(userRequests[1]);
            setPeriod(userRequests[3]);
            setAlgorithm(userRequests[5]);
            setOutput(userRequests[7]);
        } else {
            log.info("Я вас не понимаю, повторите свой запрос.");
        }
    }

    private void setCurrencyName(String userRequest) {
        try {
            currencyName = CurrencyNames.valueOf(userRequest);
        } catch (IllegalArgumentException e) {
            log.info(String.format("Некоректный ввод валюты - \"%s\"", userRequest));
        }
    }

    private void setDate(String userRequest) {
        try {
            if ("tomorrow".equals(userRequest)) {
                date = LocalDate.now().plusDays(1);
            } else {
                date = LocalDate.parse(userRequest, DateTimeFormatter.ofPattern("dd.MM.y"));
            }
            countDays = 1;
        } catch (DateTimeParseException e) {
            log.info(String.format("Некоректный ввод даты - \"%s\"", userRequest));
        }
    }

    private void setPeriod(String userRequest) {
        try {
            ForecastPeriod period = ForecastPeriod.valueOf(userRequest.toUpperCase());
            countDays = period.getDayCount();
            date = LocalDate.now().plusDays(1);
        } catch (IllegalArgumentException e) {
            log.info(String.format("Некоректный ввод периода - \"%s\"", userRequest));
        }
    }

    private void setAlgorithm(String userRequest) {
        try {
            alg = AlgorithmNames.valueOf(userRequest.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.info(String.format("Некоректный ввод алгоритма - \"%s\"", userRequest));
        }
    }

    private void setOutput(String userRequest) {
        try {
            output = Views.valueOf(userRequest.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.info(String.format("Некоректный ввод способа вывода результата - \"%s\"", userRequest));
        }
    }
}
