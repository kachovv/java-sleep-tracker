package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.util.List;

public class TotalSessionsFunction implements SleepAnalysisFunction<Integer> {
    @Override
    public Integer compute(List<SleepingSession> sessions) {
        return sessions.size();
    }

    @Override
    public String getDescription() {
        return "Общее количество сессий сна";
    }
}
