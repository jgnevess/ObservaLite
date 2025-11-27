package com.example.ObservaLite.services.utils;

import com.example.ObservaLite.utils.UrlBuilder;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

public abstract class ConnectionService {

    public static void Connect(String url) {
        try {
            url = UrlBuilder.UrlBuilder(url);
            URI uri = URI.create(url);
            String host = uri.getHost();
            if (host == null) {
                throw new RuntimeException("Host inválido na URL: " + url);
            }
            InetAddress[] addresses = InetAddress.getAllByName(host);
        } catch (UnknownHostException e) {
            throw new RuntimeException("DNS não resolveu para: " + url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
