package com.example.ObservaLite.service;

import com.example.ObservaLite.services.HealthCheckService;
import com.example.ObservaLite.utils.UrlBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HealthCheckServiceTest {

    @Test
    void testUrlBuilder() {
        String url = "www.google.com.br";
        var res = UrlBuilder.UrlBuilder(url);

        assertEquals("https://www.google.com.br", res);
    }

    @Test
    void testUrlBuilderHttps() {
        String url = "https://www.google.com.br";
        var res = UrlBuilder.UrlBuilder(url);

        assertEquals("https://www.google.com.br", res);
    }

    @Test
    void testUrlBuilderHttp() {
        String url = "http://www.google.com.br";
        var res = UrlBuilder.UrlBuilder(url);

        assertEquals("http://www.google.com.br", res);
    }
}
