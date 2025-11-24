package com.example.ObservaLite.services;

import com.example.ObservaLite.dtos.HealthCheckResponse;
import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.entities.LogEntry;
import com.example.ObservaLite.entities.Project;
import com.example.ObservaLite.exceptions.ProjectNotFoundException;
import com.example.ObservaLite.repositories.ExceptionLogRepository;
import com.example.ObservaLite.repositories.ProjectRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class Worker {

    private final ProjectRepository projectRepository;
    private final HealthCheckService healthCheckService;
    private final ExceptionLogRepository exceptionLogRepository;
    private final LogEntryService logEntryService;

    public Worker(ProjectRepository projectRepository,
                  HealthCheckService healthCheckService,
                  ExceptionLogRepository exceptionLogRepository,
                  LogEntryService logEntryService) {
        this.projectRepository = projectRepository;
        this.healthCheckService = healthCheckService;
        this.exceptionLogRepository = exceptionLogRepository;
        this.logEntryService = logEntryService;
    }

    public void runNow(UUID projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException(404, "Project Not Found"));
        try {

            HealthCheckResponse result = healthCheckService.runCheck(project);
            LogEntry logEntry = logEntryService.createLog(result);
        } catch (IOException | InterruptedException ex) {
            ExceptionLog exceptionLog = new ExceptionLog();
            exceptionLog.setProject(project);
            exceptionLog.setMessage(ex.getMessage());
            exceptionLogRepository.save(exceptionLog);
        }
    }

    @Scheduled(fixedDelay = 3600000)
//    @Scheduled(fixedDelay = 60000)
    public void scheduleAll() {
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            try {
                runNow(project.getId());
            }
            catch (ProjectNotFoundException ex) {
                ExceptionLog exceptionLog = new ExceptionLog();
                exceptionLog.setProject(project);
                exceptionLog.setMessage(ex.getMessage());
                exceptionLogRepository.save(exceptionLog);
            }
        }
    }
}
