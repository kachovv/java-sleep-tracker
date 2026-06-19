package ru.yandex.practicum.sleeptracker;

public enum Chronotype {
    LARK("Жаворонок"),
    OWL("Сова"),
    DOVE("Голубь");

    private final String russianName;

    Chronotype(String russianName) {
        this.russianName = russianName;
    }

    @Override
    public String toString() {
        return russianName;
    }
}
