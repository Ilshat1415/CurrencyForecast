package ru.liga.telegram.commands.service;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import ru.liga.telegram.Utils;

@Slf4j
public class StartCommand extends ServiceCommand {

    public StartCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);

        log.debug("Пользователь {}. Начато выполнение команды {}", userName,
                this.getCommandIdentifier());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                "Приветствую вас, " + userName + "!\n" +
                        "Давайте начнём! Если Вам нужна помощь, нажмите /help");
        log.debug("Пользователь {}. Завершено выполнение команды {}", userName,
                this.getCommandIdentifier());

    }
}
