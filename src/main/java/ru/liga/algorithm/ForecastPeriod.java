package ru.liga.algorithm;

/**
 * Временные периоды
 */
public enum ForecastPeriod {
    /**
     * Завтрашний день
     */
    MONTH(30),
    /**
     * Неделя или семь дней
     */
    WEEK(7);

    /**
     * Количество дней
     */
    private final int daysCount;

    /**
     * Создает объект, задавая количество дней.
     *
     * @param daysCount Количество дней
     */
    ForecastPeriod(int daysCount) {
        this.daysCount = daysCount;
    }

    /**
     * Возвращает количество дней для прогноза.
     *
     * @return Количество дней для прогноза
     */
    public int getDayCount() {
        return daysCount;
    }
}
