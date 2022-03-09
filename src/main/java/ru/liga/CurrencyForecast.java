package ru.liga;

import ru.liga.model.FileSystem;
import ru.liga.model.Model;
import ru.liga.view.ConsoleView;
import ru.liga.view.View;

public class CurrencyForecast {

    public static void main(String[] args) {
        Model model = new FileSystem();
        View view = new ConsoleView();

        Controller controller = new Controller(model, view);
        controller.makeCurrencyForecast();
    }
}
