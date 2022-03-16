package ru.liga.telegram.commands.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.liga.telegram.Utils;

/**
 * Команда "Помощь"
 */
@Slf4j
public class HelpCommand extends ServiceCommand {

    public HelpCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        log.debug("Пользователь {}. Начато выполнение команды {}", userName,
                this.getCommandIdentifier());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Я бот, который делает прогноз курса валют.\n\n" +
                        "Для получения прогноза введите запрос по шаблону:\n" +
                        "1. \"rate *валюта* -date *день* -alg *алгоритм*\"\n" +
                        "2. \"rate *валюта* -period *период* -alg *алгоритм* -output *вид*\"\n\n" +
                        "где *валюта*\\* - \"AMD\", \"BGN\", \"USD\", \"EUR\", \"TRY\";\n" +
                        "       *день* - \"DD.MM.YYYY\", \"tomorrow\";\n" +
                        "       *период* - \"week\", \"month\";\n" +
                        "       *алгоритм* - \"actual\", \"moon\", \"regression\";\n" +
                        "       *вид* - \"list\", \"graph\".\n" +
                        " \\* - можно указать сразу несколько валют через запятую без пробелов.\n\n" +
                        "Пример запроса:\n" +
                        "rate USD -date tomorrow -alg actual");
        log.debug("Пользователь {}. Завершено выполнение команды {}", userName,
                this.getCommandIdentifier());
    }
}
