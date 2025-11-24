package com.example.ObservaLite.dtos;

import com.example.ObservaLite.entities.HealthCheckResult;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HealthCheckResponse {
    private HealthCheckResult result;
    private String userAgent;

    public HealthCheckResponse(HealthCheckResult result, String userAgent) {
        this.result = result;
        this.userAgent = userAgent;
    }
}
