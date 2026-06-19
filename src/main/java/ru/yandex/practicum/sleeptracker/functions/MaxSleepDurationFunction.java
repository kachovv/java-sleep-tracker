package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;

public class MaxSleepDurationFunction implements SleepAnalysisFunction<Long> {
    @Override
    public Long compute(List<SleepingSession> sessions) {
        return sessions.stream()
                .mapToLong(s -> Duration.between(s.getStart(), s.getEnd()).toMinutes())
                .max()
                .orElse(0L);
    }

    @Override
    public String getDescription() {
        return "Максимальная продолжительность сна (в мин)";
    }
}
