package com.example.ObservaLite.services;

import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.entities.HealthCheckResult;
import com.example.ObservaLite.exceptions.NotFoundException;
import com.example.ObservaLite.repositories.ExceptionLogRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LogsExceptionService {

    private final ExceptionLogRepository exceptionLogRepository;

    public LogsExceptionService(ExceptionLogRepository exceptionLogRepository) {
        this.exceptionLogRepository = exceptionLogRepository;
    }

    public Page<ExceptionLog> getByProjectIdAndPeriod(UUID userId, UUID projectId, int pageNumber, int pageSize, LocalDate start, LocalDate end) {
        Instant startInstant = start.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endInstant = end.atStartOfDay(ZoneOffset.UTC).plusHours(23).plusMinutes(59).plusSeconds(59).toInstant();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ExceptionLog> page = exceptionLogRepository.findByProjectId(projectId, pageable);
        if(!page.get().toList().get(0).getProject().getUser().getId().equals(userId)) throw new NotFoundException(404, "Project not found");
        List<ExceptionLog> filtered = page.getContent().stream()
                .filter(el -> el.getProject().getLastCheckedAt().isAfter(startInstant) && el.getProject().getLastCheckedAt().isBefore(endInstant))
                .collect(Collectors.toList());
        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    public Page<ExceptionLog> getByProjectId(UUID userId, UUID projectId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ExceptionLog> page = exceptionLogRepository.findByProjectId(projectId, pageable);
        if(!page.get().toList().get(0).getProject().getUser().getId().equals(userId)) throw new NotFoundException(404, "Project not found");
        return page;
    }

    public ExceptionLog getById(UUID userId, UUID id) {
        ExceptionLog exceptionLog = exceptionLogRepository.findById(id).orElseThrow(() -> new NotFoundException(404, "Exception log not found"));
        if(exceptionLog.getProject().getUser().getId().equals(userId)) throw new NotFoundException(404, "Project not found");
        return exceptionLog;
    }
}
