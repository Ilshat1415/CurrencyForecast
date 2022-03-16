package ru.liga.telegram;

import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import ru.liga.algorithm.ActualAlgorithm;
import ru.liga.model.CurrencyName;
import ru.liga.view.GraphView;

import java.time.LocalDate;

public class UserOptionsTest extends TestCase {
    private final UserOptions userOptions = new UserOptions();

    public void testSetRate() {
        Assertions.assertThat(userOptions.setRate("AMD,BGN,EUR,TRY,USD")).isTrue();
        Assertions.assertThat(userOptions.getCurrencyNames().toArray()).isEqualTo(CurrencyName.values());
    }

    public void testSetDate() {
        Assertions.assertThat(userOptions.setDate("01.01.1970")).isTrue();
        Assertions.assertThat(userOptions.getDate()).isEqualTo(LocalDate.of(1970, 1, 1));
    }

    public void testSetView() {
        Assertions.assertThat(userOptions.setView("graph")).isTrue();
        Assertions.assertThat(userOptions.getView() instanceof GraphView).isTrue();
    }

    public void testSetAlgorithm() {
        Assertions.assertThat(userOptions.setAlgorithm("actual")).isTrue();
        Assertions.assertThat(userOptions.getAlgorithm() instanceof ActualAlgorithm).isTrue();
    }

    public void testSetCountDays() {
        Assertions.assertThat(userOptions.setCountDays("month")).isTrue();
        Assertions.assertThat(userOptions.getCountDays()).isEqualTo(30);
    }
}