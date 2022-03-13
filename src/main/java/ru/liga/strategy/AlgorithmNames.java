package ru.liga.strategy;

public enum AlgorithmNames {
    ACTUAL(new AlgorithmActual()),
    MOON(new AlgorithmMoon()),
    REGRESSION(new AlgorithmLinearRegression());

    private final Strategy strategy;

    AlgorithmNames(Strategy strategy) {
        this.strategy = strategy;
    }

    public Strategy getStrategy() {
        return strategy;
    }
}
