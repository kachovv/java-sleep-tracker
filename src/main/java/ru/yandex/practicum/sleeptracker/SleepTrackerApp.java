package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.util.List;
import java.util.ArrayList;

public class SleepTrackerApp {
    private final List<SleepAnalysisFunction<?>> functions = new ArrayList<>();

    public SleepTrackerApp() {
        functions.add(new TotalSessionsFunction());
        functions.add(new MinSleepDurationFunction());
        functions.add(new AverageSleepDurationFunction());
        functions.add(new BadQualitySessionsCountFunction());
        functions.add(new SleeplessNightsCountFunction());
        functions.add(new ChronotypeAnalysisFunction());
    }

    public void analyzeAndPrint(List<SleepingSession> sessions) {
        functions.forEach(func -> {
            SleepAnalysisResult<?> result = new SleepAnalysisResult<>(
                    func.getDescription(),
                    func.compute(sessions)
            );
            System.out.println(result.getDescription() + ": " + result.getValue());
        });
    }
    public static void main(String[] args) {

    }
}