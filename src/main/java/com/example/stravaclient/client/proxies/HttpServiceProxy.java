package com.example.stravaclient.client.proxies;

import com.example.stravaclient.client.data.*;
import java.util.HashMap;
import java.util.List;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;



public class HttpServiceProxy implements IStravaServiceProxy {

   private static final String BASE_URL = "http://localhost:8080";
   private final HttpClient httpClient;
   private final ObjectMapper objectMapper;

    public HttpServiceProxy() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String register(User user, String method, String password) {
        try{
            String userBody =objectMapper.writeValueAsString(user);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/users?method=" + method + "&pass=" + password))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(userBody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> response.body(); // Successful login, returns token
                case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials");
                default -> throw new RuntimeException("registered failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during register", e);
        }

    }

    @Override
    public String login(String email, String method, String password) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/users/auth?email=" + email + "&pass=" + password + "&method=" + method))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(""))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> response.body(); // Successful login, returns token
                case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials");
                default -> throw new RuntimeException("Login failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during login", e);
        }
    }

    @Override
    public String logout(String token) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/users/by?token=" + token))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(""))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> response.body(); // Successful login, returns token
                case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials");
                default -> throw new RuntimeException("logout failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during logout", e);
        }
    }

    // Challenge Management
    @Override
    public String createChallenge(Challenge challenge, String token) {
        try{
            String challengebody =objectMapper.writeValueAsString(challenge);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/users/challenges?token=" + token))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(challengebody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> response.body(); // Successful login, returns token
                case 401 -> throw new RuntimeException("error: Unauthorized");
                default -> throw new RuntimeException("Challenge Creation failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during creation", e);
        }
    }

    @Override
    public List<Challenge> getActiveChallenges() {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/users/challenges"))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 ->  objectMapper.convertValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Challenge.class));
                case 204 -> throw new RuntimeException("No Content: No categories found");
                case 500 -> throw new RuntimeException("Internal server error while fetching categories");case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials");
                default -> throw new RuntimeException("Login failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during login", e);
        }
    }

    @Override
    public String acceptChallenge(String token, int id) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/challenges/acceptance?token=" + token + "&id=" + id))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(""))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> response.body(); // Successful login, returns token
                case 401 -> throw new RuntimeException("Unauthorized: Invalid selection");
                default -> throw new RuntimeException("Acceptance failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during Acceptance", e);
        }
    }

    @Override
    public HashMap<String, String> getChallengeStatus(String token) {
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/challenges/progress?token=" + token))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 ->  objectMapper.convertValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Challenge.class));
                case 204 -> throw new RuntimeException("No Content: No status found");
                case 500 -> throw new RuntimeException("Internal server error while fetching categories");case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials");
                default -> throw new RuntimeException("Status failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during Status Retrieve", e);
        }
    }

    // Session Management
    @Override
    public String createSession(Session session, String token) {
        try{
            String sessionbody =objectMapper.writeValueAsString(session);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/users/sessions?token=" + token))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(sessionbody))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 -> response.body(); // Successful login, returns token
                case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials");
                default -> throw new RuntimeException("Session failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during login", e);
        }
    }

    @Override
    public List<Session> getSessions(String token) {

        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/users/sessions?token=" + token))
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return switch (response.statusCode()) {
                case 200 ->  objectMapper.convertValue(response.body(), objectMapper.getTypeFactory().constructCollectionType(List.class, Session.class));
                case 204 -> throw new RuntimeException("No Content: No categories found");
                case 500 -> throw new RuntimeException("Internal server error while fetching categories");case 401 -> throw new RuntimeException("Unauthorized: Invalid credentials");
                default -> throw new RuntimeException("Retrieval failed with status code: " + response.statusCode());
            };
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error during retrieval", e);
        }
}}