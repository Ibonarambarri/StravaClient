package com.example.stravaclient.client.proxies;


import com.example.stravaclient.client.data.*;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class RestServiceProxy implements IStravaServiceProxy {

    private final RestTemplate restTemplate;
    @Value("${stravaclient.server.url}")
    private String serverUrl;


    public RestServiceProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestServiceProxy() {

    }

    @Override
    public String register(User user, String method, String password) {
        String url =serverUrl + "/api/users";
        try {
            return restTemplate.postForObject(url + "?method=" + method + "&pass=" + password, user, String.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 400 -> throw new RuntimeException("Registration failed: Invalid input.");
                case 409 -> throw new RuntimeException("Registration failed: User already exists.");
                default -> throw new RuntimeException("Registration failed: " + e.getStatusText());
            }
        }
    }
    @Override
    public String login(String email, String method, String password) {
        String url = serverUrl + "/api/users/auth";

        try {
            return restTemplate.postForObject(url + "?email=" + email + "&method=" + method + "&pass=" + password, null, String.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 401 -> throw new RuntimeException("Login failed: Invalid credentials.");
                default -> throw new RuntimeException("Login failed: " + e.getStatusText());
            }
        }
    }
    @Override
    public String logout(String token) {
        String url = serverUrl + "/api/users/bye";

        try {
            restTemplate.postForObject(url + "?token=" + token, null, Void.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Logout failed: " + e.getStatusText());
        }
        return url;
    }

    @Override
    public User getUserByToken(String token) {
        String url = serverUrl + "/api/users/token";
        try {
            return restTemplate.getForObject(url + "?token=" + token, User.class);
        } catch (HttpStatusCodeException e) {
            switch (e.getStatusCode().value()) {
                case 401 -> throw new RuntimeException("Invalid token: User not authenticated.");
                case 404 -> throw new RuntimeException("User not found for the given token.");
                default -> throw new RuntimeException("Failed to retrieve user by token: " + e.getStatusText());
            }
        }
    }


    // Challenge Management
    @Override
    public String createChallenge(Challenge challenge, String token) {
        String url = serverUrl + "/api/challenges";

        try {
            return restTemplate.postForObject(url + "?token=" + token, challenge, String.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Challenge creation failed: " + e.getStatusText());
        }
    }
    @Override
    public List<Challenge> getActiveChallenges() {
        String url = serverUrl + "/api/challenges";

        try {
            return restTemplate.getForObject(url, List.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Failed to retrieve active challenges: " + e.getStatusText());
        }
    }
    @Override
    public String acceptChallenge( String token,Challenge challenge) {
        String url = serverUrl + "/api/challenges/acceptance";

        try {
            return restTemplate.postForObject(url + "?token=" + token + "&id=" + challenge.id(), null, String.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Failed to accept challenge: " + e.getStatusText());
        }
    }
    @Override
    public HashMap<String, String> getChallengeStatus(String token) {
        String url = serverUrl + "/api/challenges/progress";

        try {
            return restTemplate.getForObject(url + "?token=" + token, HashMap.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Failed to retrieve challenge status: " + e.getStatusText());
        }
    }

    // Session Management
    @Override
    public String createSession(Session session, String token) {
        String url = serverUrl + "/api/sessions";

        try {
            return restTemplate.postForObject(url + "?token=" + token, session, String.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Failed to create session: " + e.getStatusText());
        }
    }
    @Override
    public List<Session> getSessions(String token) {
        String url = serverUrl + "/api/sessions";

        try {
            return restTemplate.getForObject(url + "?token=" + token, List.class);
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Failed to retrieve sessions: " + e.getStatusText());
        }
    }


}
