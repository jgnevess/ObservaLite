package com.example.ObservaLite.services.auth;

import com.example.ObservaLite.entities.auth.SessionData;
import com.example.ObservaLite.entities.auth.User;
import com.example.ObservaLite.entities.auth.UserSession;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
public class SessionService {

    private final RedisTemplate<String, Object> redisTemplate;

    public SessionService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveSession(UUID sessionId, User user, Duration ttl) {
        SessionData data = new SessionData(user.getId(), user.getValidEmail());
        redisTemplate.opsForValue().set("session:" + sessionId, data, ttl);
    }

    public User getSession(UUID sessionId) {
        return (User) redisTemplate.opsForValue().get("session:" + sessionId);
    }

    public SessionData loadUser(String sessionId) {
        if (sessionId == null) return null;
        return (SessionData) redisTemplate.opsForValue().get("session:" + sessionId);
    }

    public void deleteSession(UUID sessionId) {
        redisTemplate.delete("session:" + sessionId);
    }
}
