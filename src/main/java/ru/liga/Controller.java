package ru.liga;

import lombok.extern.slf4j.Slf4j;
import ru.liga.model.CurrencyName;
import ru.liga.model.FileStorage;
import ru.liga.telegram.UserOptions;

import java.time.LocalDate;
import java.util.Map;

/**
 * Объект контроллера по прогнозу курса валют, который считывает запросы пользователя.
 * Получает данные прогноза от модели, в зависимости от переданного запроса,
 * и передаёт их для дальнейшего отображения результата пользователю.
 */
@Slf4j
public class Controller {
    /**
     * Объект пользовательских параметров.
     */
    private final UserOptions userOptions;

    /**
     * Создание объекта контроллера.
     *
     * @param userOptions объект пользовательских параметров
     */
    public Controller(UserOptions userOptions) {
        this.userOptions = userOptions;
    }

    /**
     * Осуществление прогноза курса валют.
     *
     * @return результат прогноза
     */
    public Object makeCurrencyForecast() {
        try {
            for (CurrencyName currencyName : userOptions.getCurrencyNames()) {
                Map<LocalDate, Double> data = FileStorage.getInstance().getData(currencyName);
                Map<LocalDate, Double> forecast = userOptions.getAlgorithm().getForecast(data, userOptions.getDate(),
                        userOptions.getCountDays());
                log.debug("Алгоритм {} отработал.", userOptions.getAlgorithm().getClass().getName());

                userOptions.getView().preparingResults(currencyName, forecast);
                log.debug("Представление {}. Прогноз сформирован", userOptions.getView().getClass().getName());
            }
            return userOptions.getView().getAnswer();
        } catch (NullPointerException e) {
            log.debug("Алгоритм {}. Возвращено null значение.", userOptions.getAlgorithm().getClass().getName());
            return "К сожалению алгоритм \"Актуальный\" не умеет делать прогноз на такое далёкое будущее.";
        }

    }
}
