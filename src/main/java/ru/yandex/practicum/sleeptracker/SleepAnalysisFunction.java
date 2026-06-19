package ru.yandex.practicum.sleeptracker;

import java.util.List;

public interface SleepAnalysisFunction<T> {
    T compute(List<SleepingSession> sessions);
    
    String getDescription();
}
