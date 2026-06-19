package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.functions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {
    private SleepingSession session(int durationMinutes, SleepQuality quality) {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        return new SleepingSession(start, start.plusMinutes(durationMinutes), quality);
    }

    private SleepingSession session(LocalDateTime start, LocalDateTime end, SleepQuality quality) {
        return new SleepingSession(start, end, quality);
    }

    private SleepingSession session(LocalDateTime start, LocalDateTime end) {
        return session(start, end, SleepQuality.NORMAL);
    }

    @Test
    public void totalSessionsFunctionShouldReturnTotalCount() {
        List<SleepingSession> sessions = List.of(
                session(200, SleepQuality.GOOD),
                session(300, SleepQuality.NORMAL)
        );
        TotalSessionsFunction func = new TotalSessionsFunction();
        assertEquals(2, (int) func.compute(sessions), "Должно вернуться значение 2");
        assertEquals("Общее количество сессий сна", func.getDescription());
    }

    @Test
    public void totalSessionsFunctionShouldReturn0() {
        TotalSessionsFunction func = new TotalSessionsFunction();
        assertEquals(0, func.compute(List.of()), "Пустой список должен вернуть 0");
    }

    @Test
    public void minSleepDurationShouldReturnMinValue() {
        List<SleepingSession> sessions = List.of(
                session(500, SleepQuality.GOOD),
                session(250, SleepQuality.BAD),
                session(400, SleepQuality.GOOD)
        );
        MinSleepDurationFunction func = new MinSleepDurationFunction();
        assertEquals(250L, func.compute(sessions), "Должно вернуться минимальное значение (250)");
    }

    @Test
    public void minSleepDurationShouldReturn0() {
        MinSleepDurationFunction func = new MinSleepDurationFunction();
        assertEquals(0L, func.compute(List.of()), "Пустой список должен вернуть 0");
    }

    @Test
    public void maxSleepDurationShouldReturnMaxValue() {
        List<SleepingSession> sessions = List.of(
                session(300, SleepQuality.BAD),
                session(400, SleepQuality.NORMAL),
                session(150, SleepQuality.BAD)
        );
        MaxSleepDurationFunction func = new MaxSleepDurationFunction();
        assertEquals(400L, func.compute(sessions), "Должен вернуть максимальное значение(400)");
    }

    @Test
    public void maxSleepDurationShouldReturn0() {
        MaxSleepDurationFunction func = new MaxSleepDurationFunction();
        assertEquals(0L, func.compute(List.of()), "Пустой список должен вернуть 0");
    }

    @Test
    public void averageSleepDurationShouldComputeCorrectly() {
        List<SleepingSession> sessions = List.of(
                session(100, SleepQuality.BAD),
                session(200, SleepQuality.NORMAL),
                session(300, SleepQuality.NORMAL)
        );
        AverageSleepDurationFunction func = new AverageSleepDurationFunction();
        assertEquals(200.0, func.compute(sessions), 0.001, "Должно вернуться среднее значение (200.0)");
    }

    @Test
    public void averageSleepDurationShouldReturn0() {
        AverageSleepDurationFunction func = new AverageSleepDurationFunction();
        assertEquals(0.0, func.compute(List.of()), "Пустой список должен вернуть 0");
    }

    @Test
    public void badQualitySessionsCountShouldCountOnlyBad() {
        List<SleepingSession> sessions = List.of(
                session(400, SleepQuality.BAD),
                session(300, SleepQuality.GOOD),
                session(200, SleepQuality.BAD),
                session(500, SleepQuality.NORMAL)
        );
        BadQualitySessionsCountFunction func = new BadQualitySessionsCountFunction();
        assertEquals(2L, func.compute(sessions), "Должно вернуться значение 2");
    }

    @Test
    public void badQualitySessionsCountShouldReturn0() {
        List<SleepingSession> sessions = List.of(
                session(400, SleepQuality.GOOD),
                session(300, SleepQuality.NORMAL)
        );
        BadQualitySessionsCountFunction func = new BadQualitySessionsCountFunction();
        assertEquals(0L, func.compute(sessions), "Список без SleepQuality.BAD должен вернуть 0");
    }

    @Test
    public void sleeplessNightsCountShouldReturn0() {
        SleeplessNightsCountFunction func = new SleeplessNightsCountFunction();
        assertEquals(0L, func.compute(List.of()),
                "Пустой список сессий должен давать 0 бессонных ночей");
    }

    @Test
    public void sleeplessNightsCountShouldNotCountSessionBeforeNoonCoveringNight() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 1, 5, 0);
        LocalDateTime end = LocalDateTime.of(2025, 10, 1, 7, 0);
        List<SleepingSession> sessions = List.of(session(start, end));
        SleeplessNightsCountFunction func = new SleeplessNightsCountFunction();
        assertEquals(0L, func.compute(sessions),
                "Сессия, покрывающая 0-6 предыдущей ночи, делает её не бессонной");
    }

    @Test
    public void sleeplessNightsCountShouldCountSessionsBeforeNoonNotCoveringNight() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 10, 1, 11, 0);
        List<SleepingSession> sessions = List.of(session(start, end));
        SleeplessNightsCountFunction func = new SleeplessNightsCountFunction();
        assertEquals(1L, func.compute(sessions),
                "Если сессия началась до 12 и не затронула 0-6, предыдущая ночь бессонная");
    }

    @Test
    public void sleeplessNightsCountShouldNotCountSessionsAfterNoon() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 1, 14, 0);
        LocalDateTime end = LocalDateTime.of(2025, 10, 1, 16, 0);
        List<SleepingSession> sessions = List.of(session(start, end));
        SleeplessNightsCountFunction func = new SleeplessNightsCountFunction();
        assertEquals(0L, func.compute(sessions),
                "Следующая ночь ещё не наступила, бессонных ночей нет");
    }

    @Test
    public void chronotypeAnalysisShouldReturnZeroWhenEmptySessions() {
        ChronotypeAnalysisFunction func = new ChronotypeAnalysisFunction();
        assertEquals(Chronotype.DOVE, func.compute(List.of()),
                "При отсутствии данных возвращаем Голубя");
    }

    @Test
    public void chronotypeAnalysisShouldReturnLark() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 1, 21, 0);
        LocalDateTime end = LocalDateTime.of(2025, 10, 2, 6, 0);
        SleepingSession session = new SleepingSession(start, end, SleepQuality.GOOD);
        ChronotypeAnalysisFunction func = new ChronotypeAnalysisFunction();
        assertEquals(Chronotype.LARK, func.compute(List.of(session)));
    }

    @Test
    public void chronotypeAnalysisShouldReturnOwl() {
        LocalDateTime start = LocalDateTime.of(2025, 10, 2, 0, 30);
        LocalDateTime end = LocalDateTime.of(2025, 10, 2, 10, 0);
        SleepingSession session = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 23, 30),
                LocalDateTime.of(2025, 10, 2, 10, 0),
                SleepQuality.NORMAL);
        ChronotypeAnalysisFunction func = new ChronotypeAnalysisFunction();
        assertEquals(Chronotype.OWL, func.compute(List.of(session)));
    }

    @Test
    public void chronotypeAnalysisShouldReturnDoveWhenOwlNightsCountsEqualsLarkNightsCounts() {
        SleepingSession lark = new SleepingSession(
                LocalDateTime.of(2025, 10, 1, 21, 0),
                LocalDateTime.of(2025, 10, 2, 6, 0),
                SleepQuality.GOOD);
        SleepingSession owl = new SleepingSession(
                LocalDateTime.of(2025, 10, 2, 23, 30),
                LocalDateTime.of(2025, 10, 3, 10, 0),
                SleepQuality.GOOD);
        ChronotypeAnalysisFunction func = new ChronotypeAnalysisFunction();
        assertEquals(Chronotype.DOVE, func.compute(List.of(lark, owl)),
                "При равном количестве жаворонков и сов должен быть голубь");
    }
}
