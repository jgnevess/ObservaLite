package com.example.ObservaLite.services;


import com.example.ObservaLite.dtos.HealthCheckResponse;
import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.entities.enums.IncidentType;
import com.example.ObservaLite.exceptions.NotFoundException;
import com.example.ObservaLite.repositories.ExceptionLogRepository;
import com.example.ObservaLite.repositories.HealthCheckResultRepository;
import com.example.ObservaLite.utils.UrlBuilder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HealthCheckService {

    private final HealthCheckResultRepository healthCheckResultRepository;
    private final ExceptionLogRepository exceptionLogRepository;
    private final IncidentService incidentService;

    public  HealthCheckService(HealthCheckResultRepository healthCheckResultRepository,
                               ExceptionLogRepository exceptionLogRepository,
                               IncidentService incidentService) {
        this.healthCheckResultRepository = healthCheckResultRepository;
        this.exceptionLogRepository = exceptionLogRepository;
        this.incidentService = incidentService;
    }

    public HealthCheckResponse runCheck(Project project) throws IOException, InterruptedException {
        String urlBuild = UrlBuilder.UrlBuilder(project.getUrl());
        HttpClient client = HttpClient.newHttpClient();
        String userAgent = "ObservaLite/1.0";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlBuild))
                .timeout(Duration.ofSeconds(10))
                .header("User-Agent", userAgent)
                .GET()
                .build();

        Instant start = Instant.now();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Instant end = Instant.now();

        URL url = new URL(urlBuild);
        int daysRemaining = -1;
        if (url.getProtocol().equalsIgnoreCase("https")) {
            try {
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.connect();
                X509Certificate cert = (X509Certificate) conn.getServerCertificates()[0];
                daysRemaining = (int) Instant.now().until(cert.getNotAfter().toInstant(), ChronoUnit.DAYS);
            } catch (Exception ex) {
                ExceptionLog exceptionLog = new ExceptionLog();
                exceptionLog.setProject(project);
                exceptionLog.setMessage(ex.getMessage());
                exceptionLogRepository.save(exceptionLog);
            }
        }

        InetAddress[] addresses = InetAddress.getAllByName(url.getHost());
        String dnsSummary = Arrays.stream(addresses)
                .map(InetAddress::getHostAddress)
                .collect(Collectors.joining(", "));

        HealthCheckResult result = new HealthCheckResult();
        result.setProject(project);
        result.setHealthy(response.statusCode() >= 200 && response.statusCode() < 400);
        result.setStatusCode(response.statusCode());
        result.setLatencyMs(Duration.between(start, end).toMillis());
        result.setRawResponse(response.body());
        result.setCheckedAt(Instant.now());
        result.setSslDaysRemaining(daysRemaining);
        result.setDnsSummary(dnsSummary);

        result = healthCheckResultRepository.save(result);

        if (!result.isHealthy()) {
            incidentService.handleIncident(project, IncidentType.PROJECT_DOWN,
                    "HTTP status: " + result.getStatusCode());
        } else {
            incidentService.resolveIncident(project, IncidentType.PROJECT_DOWN);
        }


        if (result.getSslDaysRemaining() >= 0 && result.getSslDaysRemaining() < 7) {
            incidentService.handleIncident(project, IncidentType.SSL_EXPIRING,
                    "SSL expires in " + result.getSslDaysRemaining() + " days");
        } else {
            incidentService.resolveIncident(project, IncidentType.SSL_EXPIRING);
        }

        Instant window = Instant.now().minus(Duration.ofMinutes(5));
        int errors = healthCheckResultRepository.countErrorsSince(project, window);

        int limit = 5;
        if (errors >= limit) {
            incidentService.handleIncident(project, IncidentType.ERROR_SPIKE,
                    "High error rate in last 5 minutes: " + errors);
        } else {
            incidentService.resolveIncident(project, IncidentType.ERROR_SPIKE);
        }

        List<HealthCheckResult> last3 = healthCheckResultRepository
                .findTop3ByProjectOrderByCheckedAtDesc(project);

        boolean allFailed = !last3.isEmpty()
                && last3.size() == 3
                && last3.stream().allMatch(h -> !h.isHealthy());

        if (allFailed) {
            incidentService.handleIncident(project, IncidentType.DOWNTIME,
                    "3 consecutive failed health checks");
        } else {
            incidentService.resolveIncident(project, IncidentType.DOWNTIME);
        }

        return new HealthCheckResponse(result, userAgent);
    }

    public Page<HealthCheckResult> getByProjectIdAndPeriod(UUID projectId, int pageNumber, int pageSize, LocalDate start, LocalDate end) {
        Instant startInstant = start.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endInstant = end.atStartOfDay(ZoneOffset.UTC).plusHours(23).plusMinutes(59).plusSeconds(59).toInstant();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("checkedAt").descending());
        Page<HealthCheckResult> page = healthCheckResultRepository.findByProjectId(projectId, pageable);
        List<HealthCheckResult> filtered = page.getContent().stream()
                .filter(hc -> hc.getCheckedAt().isAfter(startInstant) && hc.getCheckedAt().isBefore(endInstant))
                .collect(Collectors.toList());
        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    public Page<HealthCheckResult> getByProjectId(UUID projectId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("checkedAt").descending());
        return healthCheckResultRepository.findByProjectId(projectId, pageable);
    }

    public HealthCheckResult getById(UUID healthCheckId) {
        return healthCheckResultRepository.findById(healthCheckId).orElseThrow(() -> new NotFoundException(404 ,"Health check not found"));
    }

}
