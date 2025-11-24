package com.example.ObservaLite.services;

import com.example.ObservaLite.entities.ExceptionLog;
import com.example.ObservaLite.exceptions.NotFoundException;
import com.example.ObservaLite.repositories.ExceptionLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LogsExceptionService {

    private final ExceptionLogRepository exceptionLogRepository;

    public LogsExceptionService(ExceptionLogRepository exceptionLogRepository) {
        this.exceptionLogRepository = exceptionLogRepository;
    }

    public Page<ExceptionLog> getByProjectId(UUID projectId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return exceptionLogRepository.findByProjectId(projectId, pageable);
    }

    public ExceptionLog getById(UUID id) {
        return exceptionLogRepository.findById(id).orElseThrow(() -> new NotFoundException(404, "Exception log not found"));
    }
}
