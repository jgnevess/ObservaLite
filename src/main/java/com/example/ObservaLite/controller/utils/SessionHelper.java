package com.example.ObservaLite.controller.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public abstract class SessionHelper {

    public static String getSessionId(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("session_id")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
