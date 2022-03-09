package ru.liga.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Консольное представление, которое отображает результат работы программы пользователю
 * и обеспечивает работу с консолью.
 */
public class ConsoleView implements View {
    private final Logger log = LoggerFactory.getLogger(ConsoleView.class);
    /**
     * Шаблон форматирования даты.
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("E dd.MM.y");
    /**
     * Поток ввода символов в консоли.
     */
    private final BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void displayForecast(Map<LocalDate, Double> forecastData) {
        forecastData.forEach((key, value) -> writeMessage(key.format(DATE_TIME_FORMATTER) + String.format(" - %.2f", value)));
    }

    @Override
    public void writeMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String readString() {
        String line = "";
        try {
            line = consoleReader.readLine();
        } catch (IOException e) {
            log.error("При чтении запроса возникла ошибка", e);
        }
        return line;
    }
}
