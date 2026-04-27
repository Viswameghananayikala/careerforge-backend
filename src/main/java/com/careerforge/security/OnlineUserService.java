package com.careerforge.security;

import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OnlineUserService {

    private final ConcurrentHashMap<String, Long> onlineUsers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> lastSeenMap = new ConcurrentHashMap<>();

public void userOffline(String email) {
    onlineUsers.remove(email);
    lastSeenMap.put(email, System.currentTimeMillis());
}

public long getLastSeenTime(String email) {
    return lastSeenMap.getOrDefault(email, 0L);
}

    public void userOnline(String email) {
        onlineUsers.put(email, System.currentTimeMillis());
    }

    public boolean isOnline(String email) {
        return onlineUsers.containsKey(email);
    }

    public long getLastSeen(String email) {
        return onlineUsers.getOrDefault(email, 0L);
    }
}