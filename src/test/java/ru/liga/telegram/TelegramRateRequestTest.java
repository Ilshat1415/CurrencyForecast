package ru.liga.telegram;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.liga.algorithm.ActualAlgorithm;
import ru.liga.algorithm.MoonAlgorithm;
import ru.liga.source.CurrencyName;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TelegramRateRequestTest {

    @Test
    public void testCreateRequestFirst() {
        TelegramRateRequest requestFirst = new TelegramRateRequest("rate TRY -date 21.06.2022 -alg actual");

        Assertions.assertThat(requestFirst.getCurrencyNames().get(0)).isEqualTo(CurrencyName.TRY);
        Assertions.assertThat(requestFirst.getDate()).isEqualTo(LocalDate.of(2022, 6, 21));
        Assertions.assertThat(requestFirst.getAlgorithm().getClass()).isEqualTo(ActualAlgorithm.class);
    }

    @Test
    public void testCreateRequestSecond() {
        TelegramRateRequest requestSecond = new TelegramRateRequest("rate USD,EUR -period month -alg moon -output graph");

        List<CurrencyName> currencyNames = new ArrayList<>();
        currencyNames.add(CurrencyName.USD);
        currencyNames.add(CurrencyName.EUR);

        Assertions.assertThat(requestSecond.getCurrencyNames()).isEqualTo(currencyNames);
        Assertions.assertThat(requestSecond.getCountDays()).isEqualTo(30);
        Assertions.assertThat(requestSecond.getAlgorithm().getClass()).isEqualTo(MoonAlgorithm.class);
        Assertions.assertThat(requestSecond.getOutput()).isEqualTo("graph");
    }
}