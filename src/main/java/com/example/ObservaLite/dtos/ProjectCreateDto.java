package com.example.ObservaLite.dtos;

import com.example.ObservaLite.services.utils.DurationDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Duration;

public record ProjectCreateDto(
        String name,
        String url,
        String apiKey,
        @JsonDeserialize(using = DurationDeserializer.class)
        @Schema(type = "object", example = "{ \"seconds\": 30 }")
        Duration checkInterval
) {
}
