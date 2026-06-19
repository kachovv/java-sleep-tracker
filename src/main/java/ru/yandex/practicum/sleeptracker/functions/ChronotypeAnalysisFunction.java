package ru.yandex.practicum.sleeptracker.functions;

import ru.yandex.practicum.sleeptracker.Chronotype;
import ru.yandex.practicum.sleeptracker.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.SleepQuality;
import ru.yandex.practicum.sleeptracker.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

public class ChronotypeAnalysisFunction implements SleepAnalysisFunction<Chronotype> {
    @Override
    public Chronotype compute(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return Chronotype.DOVE; // при пустом списке возвращаем Голубь
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
            return Chronotype.DOVE;
        }

        long[] counts = new long[3]; // cчетчики для каждого хронотипа: индекс 0 - Жаворонок, 1 - Сова, 2 - Голубь

        LongStream.rangeClosed(0, daysBetween)
                .mapToObj(firstNight::plusDays)
                .forEach(date -> {
                    Optional< SleepingSession> nightSession = findNightSession(date, sessions);
                    nightSession.ifPresent(s -> {
                        LocalTime sleepTime = s.getStart().toLocalTime();
                        LocalTime wakeTime = s.getEnd().toLocalTime();

                        if (sleepTime.isAfter(LocalTime.of(23, 0)) && wakeTime.isBefore(LocalTime.of(7, 0))) {
                            counts[1]++; // это Сова
                        } else if (sleepTime.isBefore(LocalTime.of(22, 0)) && wakeTime.isBefore(LocalTime.of(7, 0))) {
                            counts[0]++; // это Жаворонок
                        } else {
                            counts[2]++; // это Голубь
                        }
                    });
                });
        long lark = counts[0];
        long owl = counts[1];
        long dove = counts[2];

        if (lark == owl || lark == dove || owl == dove) {
            return Chronotype.DOVE; // если количество ночей двух типов совпадает, то возвращаем Голубя
        }
        // иначе возвращаем макс.
        if (lark > owl && lark > dove) {
            return Chronotype.LARK;
        } else if (owl > lark && owl > dove) {
            return Chronotype.OWL;
        } else {
            return Chronotype.DOVE;
        }
    }

    private Optional<SleepingSession> findNightSession(LocalDate date, List<SleepingSession> sessions) {
        LocalDateTime nightStart = date.atStartOfDay();
        LocalDateTime nightEnd = date.atTime(6, 0);

        return sessions.stream()
                .filter(s -> s.getStart().isBefore(nightEnd) && s.getEnd().isAfter(nightStart))
                .findFirst();
    }

    @Override
    public String getDescription() {
        return "Хронотип пользователя";
    }
}
