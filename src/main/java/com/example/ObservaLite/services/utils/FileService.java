package com.example.ObservaLite.services.utils;

import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.Incident;
import com.example.ObservaLite.entities.LogEntry;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public abstract class FileService {
    private static final String REPORTS_PATH =
            System.getProperty("user.dir") + "/src/reports/";

    public static String genReportHealthCheck(List<HealthCheckResult> healthCheckResults) {
        Date dateTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder("Relatório gerado:, " + sdf.format(dateTime) + "\n").append("\n");

        sb.append("Project name, " +
                "Status code, " +
                "Latency MS, " +
                "SSL days remaining, " +
                "DNS summary, " +
                "checked At," +
                "healthy\n").append("\n");

        healthCheckResults.forEach(h ->
                sb.append(h.getProject().getName()).append(",")
                        .append(h.getStatusCode()).append(",")
                        .append(h.getLatencyMs()).append(",")
                        .append(h.getSslDaysRemaining()).append(",")
                        .append(h.getDnsSummary()).append(",")
                        .append(h.getCheckedAt()).append(",")
                        .append(h.isHealthy()).append("\n")
        );
        try {
            String filename = REPORTS_PATH + "health_check" + UUID.randomUUID() + ".csv";
            Path path = Path.of(filename);
            Files.writeString(path, sb.toString());
            return filename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String genReportLogEntry(List<LogEntry> logEntries) {
        Date dateTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder("Relatório gerado:, " + sdf.format(dateTime) + "\n").append("\n");

        sb.append("Project name, " +
                "Message, " +
                "Level, " +
                "Timestamp, " +
                "Durations MS, " +
                "IP," +
                "User agent," +
                "Extra info\n").append("\n");

        logEntries.forEach(h ->
                sb.append(h.getProject().getName()).append(",")
                        .append(h.getMessage()).append(",")
                        .append(h.getLevel()).append(",")
                        .append(h.getTimestamp()).append(",")
                        .append(h.getMetadata().getDurationMs()).append(",")
                        .append(h.getMetadata().getIp()).append(",")
                        .append(h.getMetadata().getUserAgent()).append(",")
                        .append(h.getMetadata().getExtraInfo()).append("\n")
        );
        try {
            String filename = REPORTS_PATH+ "log_entry" + UUID.randomUUID() + ".csv";
            Path path = Path.of(filename);
            Files.writeString(path, sb.toString());
            return filename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String genReportIncidents(List<Incident> incidents) {
        Date dateTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder("Relatório gerado:, " + sdf.format(dateTime) + "\n").append("\n");

        sb.append("Project name, " +
                "Details, " +
                "Type, " +
                "Status, " +
                "Occurred at, " +
                "Resolved at\n").append("\n");

        incidents.forEach(h ->
                sb.append(h.getProject().getName()).append(",")
                        .append(h.getDetails()).append(",")
                        .append(h.getType()).append(",")
                        .append(h.getStatus()).append(",")
                        .append(h.getOccurredAt()).append(",")
                        .append(h.getResolvedAt()).append("\n")
        );
        try {
            String filename = REPORTS_PATH + "log_entry" + UUID.randomUUID() + ".csv";
            Path path = Path.of(filename);
            Files.writeString(path, sb.toString());
            return filename;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
