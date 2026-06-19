package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class SleeplessNightsCountFunction implements SleepAnalysisFunction<Long> {
    @Override
    public Long compute(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return 0L;
        }
        SleepingSession first = sessions.get(0);
        SleepingSession last = sessions.get(sessions.size() - 1);
        LocalDateTime startFirst = first.getStart();
        LocalDateTime endLast = last.getEnd();
        LocalDate firstNight;

        if (startFirst.getHour() < 12) {
            firstNight = startFirst.toLocalDate();
        } else {
            firstNight = startFirst.toLocalDate().plusDays(1);
        }

        LocalDate lastNight = endLast.toLocalDate();
        long daysBetween = ChronoUnit.DAYS.between(firstNight, lastNight);

        if (daysBetween < 0) {
            return 0L;
        }
        List<LocalDate> allNights = LongStream.rangeClosed(0, daysBetween)
                .mapToObj(firstNight::plusDays)
                .collect(Collectors.toList());

        long sleeplessNightsCount = allNights.stream()
                .filter(date -> isSleeplessNight(date, sessions))
                .count();

        return sleeplessNightsCount;
    }

    private boolean isSleeplessNight(LocalDate date, List<SleepingSession> sessions) {
        LocalDateTime nightStart = date.atStartOfDay();
        LocalDateTime nightEnd = date.atTime(6, 0);

        return sessions.stream()
                .noneMatch(s -> s.getStart().isBefore(nightEnd) && s.getEnd().isAfter(nightStart));
    }

    @Override
    public String getDescription() {
        return "Количество бессонных ночей";
    }
}
