package com.example.ObservaLite.services.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.time.Duration;

public class DurationDeserializer extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (!node.has("seconds")) {
            throw new IOException("Formato de Duration inválido");
        } else {
            long seconds = node.get("seconds").asLong();
            return Duration.ofSeconds(seconds);
        }
    }
}
