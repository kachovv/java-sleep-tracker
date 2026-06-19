package ru.yandex.practicum.sleeptracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SleepLogReader {
    public List<SleepingSession> readFromResource(String resourceName) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);

        if (is == null) {
            throw  new IllegalArgumentException("Файл не найден в classpath: " + resourceName);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            return br.lines()
                    .map(this::parseLine)
                    .collect(Collectors.toList());
        }
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Неверный формат строки: " + line);
        }
        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), FORMATTER);
        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim().toUpperCase());

        return new SleepingSession(start, end, quality);
    }
}
