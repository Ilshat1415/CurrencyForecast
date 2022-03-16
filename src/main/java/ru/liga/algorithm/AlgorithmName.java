package ru.liga.algorithm;

/**
 * Список алигоритмов
 */
public enum AlgorithmName {
    /**
     * Алгоритм “Актуальный”
     */
    ACTUAL(new ActualAlgorithm()),
    /**
     * Алгоритм "Мистический"
     */
    MOON(new MoonAlgorithm()),
    /**
     * Алгоритм линейной регресии
     */
    REGRESSION(new RegressionAlgorithm());

    /**
     * Объект выбранного алгоритма
     */
    private final Algorithm algorithm;

    /**
     * Создаёт объект, устанавливая алгоритм.
     *
     * @param algorithm объект алгоритма
     */
    AlgorithmName(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Возвращает выбранный объект алгоритма.
     *
     * @return объект алгоритма
     */
    public Algorithm getAlgorithm() {
        return algorithm;
    }
}
