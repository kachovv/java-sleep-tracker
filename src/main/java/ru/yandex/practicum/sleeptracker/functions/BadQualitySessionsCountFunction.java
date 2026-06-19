package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class BadQualitySessionsCountFunction implements SleepAnalysisFunction<Long> {
    @Override
    public Long compute(List<SleepingSession> sessions) {
        return sessions.stream()
                .filter(s -> s.getQuality() == SleepQuality.BAD)
                .count();
    }

    @Override
    public String getDescription() {
        return "Количество сессий с плохим количеством сна";
    }
}
