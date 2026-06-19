package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.Duration;
import java.util.List;

public class MinSleepDurationFunction implements SleepAnalysisFunction<Long> {
    @Override
    public Long compute(List<SleepingSession> sessions) {
        return sessions.stream()
                .mapToLong(s -> Duration.between(s.getStart(), s.getEnd()).toMinutes())
                .min()
                .orElse(0L);
    }

    @Override
    public String getDescription() {
        return "Минимальная продолжительность сессии сна (в мин.)";
    }
}
