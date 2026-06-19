package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;

public class AverageSleepDurationFunction implements SleepAnalysisFunction<Double> {
    @Override
    public Double compute(List<SleepingSession> sessions) {
        return sessions.stream()
                .mapToDouble(s -> Duration.between(s.getStart(), s.getEnd()).toMinutes())
                .average()
                .orElse(0.0);
    }

    @Override
    public String getDescription() {
        return "Средняя продолжительность сессии сна (в мин)";
    }
}
