package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;

import java.util.List;
import java.util.ArrayList;

public class SleepTrackerApp {
    private final List<SleepAnalysisFunction<?>> functions = new ArrayList<>();

    public SleepTrackerApp() {
        functions.add(new TotalSessionsFunction());
        functions.add(new MinSleepDurationFunction());
        functions.add(new MaxSleepDurationFunction());
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
        if (args.length == 0) {
            System.out.println("Укажите имя файла ресурса");
            return;
        }
        SleepLogReader reader = new SleepLogReader();
        List<SleepingSession> sessions;
        try {
            sessions = reader.readFromResource(args[0]);
        } catch (Exception e) {
            System.err.println("Ошибка загрузки данных: " + e.getMessage());
            return;
        }

        SleepTrackerApp app = new SleepTrackerApp();
        app.analyzeAndPrint(sessions);
    }
}