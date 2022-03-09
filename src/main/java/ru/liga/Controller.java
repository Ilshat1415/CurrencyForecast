package ru.liga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.liga.model.CurrencyNames;
import ru.liga.model.Model;
import ru.liga.strategy.ArithmeticMeanForecast;
import ru.liga.strategy.ForecastPeriod;
import ru.liga.strategy.Strategy;
import ru.liga.view.View;

import java.time.LocalDate;
import java.util.Map;

/**
 * Объект контроллера по прогнозу курса валют, который считывает запросы пользователя.
 * Получает данные прогноза от модели, в зависимости от переданного запроса,
 * и передаёт их для дальнейшего отображения результата пользователю.
 */
public class Controller {
    /**
     * Модель.
     */
    private final Model model;
    /**
     * Вид программы.
     */
    private final View view;
    private final Logger log = LoggerFactory.getLogger(Controller.class);

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
        Strategy strategy = new ArithmeticMeanForecast();

        String[] userRequests;
        while (!"exit".equalsIgnoreCase((userRequests = getUserRequests())[0])) {
            if (userRequests.length != 3) {
                log.info("Введён не корректный запрос, повторите");
            } else {
                if ("rate".equals(userRequests[0])) {
                    try {
                        CurrencyNames currencyName = CurrencyNames.valueOf(userRequests[1]);
                        int period = getPeriod(userRequests[2]);
                        Map<LocalDate, Double> data = model.getData(currencyName);
                        Map<LocalDate, Double> forecast = strategy.getForecast(data, period);
                        view.displayForecast(forecast);
                    } catch (IllegalArgumentException e) {
                        log.info(String.format("Некоректный ввод названия валюты - \"%s\"", userRequests[1]));
                    } catch (RuntimeException e) {
                        log.info(String.format("Некоректный ввод периода прогнозирования - \"%s\"", userRequests[2]));
                    }
                } else {
                    log.info(String.format("Команда \"%s\" не поддерживается, повторите ввод", userRequests[0]));
                }
            }
        }
    }

    /**
     * Конвертирует строковое название периода прогнозирования в целочисленное значение.
     *
     * @param periodName Название периода
     * @return Период в числовом виде
     */
    private int getPeriod(String periodName) {
        try {
            return ForecastPeriod.valueOf(periodName.toUpperCase()).getDayCount();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает массив из запроса пользователя.
     *
     * @return Запросы пользователя
     */
    private String[] getUserRequests() {
        view.writeMessage("\nВведите запрос или \"exit\" для выхода:");
        return view.readString().split(" ");
    }
}
