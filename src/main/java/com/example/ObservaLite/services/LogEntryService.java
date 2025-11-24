package com.example.ObservaLite.services;

import com.example.ObservaLite.dtos.HealthCheckResponse;
import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.LogEntry;
import com.example.ObservaLite.entities.utils.Metadata;
import com.example.ObservaLite.repositories.LogEntryRepository;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class LogEntryService {

    private final LogEntryRepository logEntryRepository;

    public LogEntryService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    public void createLog(HealthCheckResponse healthCheckResponse) {
        HealthCheckResult healthCheckResult = healthCheckResponse.getResult();
        LogEntry logEntry = new LogEntry();
        LogLevel level;
        if (healthCheckResult.getStatusCode() >= 200 && healthCheckResult.getStatusCode() < 300) level = LogLevel.INFO;
        else if (healthCheckResult.getStatusCode() >= 300 && healthCheckResult.getStatusCode() < 400) level = LogLevel.WARN;
        else level = LogLevel.ERROR;
        String message = healthCheckResult.isHealthy() ? "Healthy" : "Not healthy";
        Metadata metadata = new Metadata();
        metadata.setIp(healthCheckResult.getDnsSummary());
        metadata.setDurationMs(healthCheckResult.getLatencyMs());
        metadata.setExtraInfo("Status code: " + healthCheckResult.getStatusCode() + " SSL days remaining: " + healthCheckResult.getSslDaysRemaining());
        metadata.setUserAgent(healthCheckResponse.getUserAgent());

        logEntry.setProject(healthCheckResult.getProject());
        logEntry.setMessage(message);
        logEntry.setTimestamp(Instant.now());
        logEntry.setLevel(level);
        logEntry.setMetadata(metadata);

        var res = logEntryRepository.save(logEntry);
    }
}
