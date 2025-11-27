package com.example.ObservaLite.dtos;

import com.example.ObservaLite.services.utils.DurationDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Duration;
import java.util.UUID;

@Schema(description = "DTO para criar um projeto")
public record ProjectCreateDto(
        @Schema(description = "Nome do projeto")
        String name,
        @Schema(description = "URL do projeto")
        String url,
        @Schema(type = "text", description = "A sua chave de api será criptografada em nosso banco de dados")
        String apiKey,
        @Schema(type = "integer", example = "300", description = "Intervalo em segundos")
        long checkInterval
) {
}
