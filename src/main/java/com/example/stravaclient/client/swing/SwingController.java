package com.example.stravaclient.client.swing;

import com.example.stravaclient.client.data.*;
import com.example.stravaclient.client.proxies.IStravaServiceProxy;
import com.example.stravaclient.client.proxies.RestServiceProxy;

import java.util.HashMap;
import java.util.List;

public class SwingController
{

    private IStravaServiceProxy serviceProxy= new RestServiceProxy();
    private String token;

    public String register(User user, String method, String password) {
        try {
            return serviceProxy.register(user, method, password);
        } catch (RuntimeException e) {
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    public String login(String email, String method, String password) {
        try {
            token = serviceProxy.login(email, method, password);
            return token;
        } catch (RuntimeException e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    public void logout() {
        try {
            serviceProxy.logout(token);
            token = null;
        } catch (RuntimeException e) {
            throw new RuntimeException("Logout failed: " + e.getMessage());
        }
    }
    public User getUserByToken() {
        try {
            return serviceProxy.getUserByToken(token);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to retrieve user: " + e.getMessage());
        }
    }

    /**
     * Creates a new challenge.
     * @param challenge The challenge details.
     * @return The response string from the server.
     */
    public String createChallenge(Challenge challenge) {
        try {
            return serviceProxy.createChallenge(challenge, token);
        } catch (RuntimeException e) {
            throw new RuntimeException("Challenge creation failed: " + e.getMessage());
        }
    }

    /**
     * Retrieves the list of active challenges.
     * @return A list of active challenges.
     */
    public List<Challenge> getActiveChallenges() {
        try {
            return serviceProxy.getActiveChallenges();
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to retrieve active challenges: " + e.getMessage());
        }
    }

    /**
     * Accepts a challenge.
     * @param challenge The challenge to accept.
     * @return The response string from the server.
     */
    public String acceptChallenge(Challenge challenge) {
        try {
            return serviceProxy.acceptChallenge(token, challenge);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to accept challenge: " + e.getMessage());
        }
    }

    /**
     * Retrieves the status of the user's challenges.
     * @return A map of challenge statuses.
     */
    public HashMap<String, String> getChallengeStatus() {
        try {
            return serviceProxy.getChallengeStatus(token);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to retrieve challenge status: " + e.getMessage());
        }
    }

    /**
     * Creates a new session.
     * @param session The session details.
     * @return The response string from the server.
     */
    public String createSession(Session session) {
        try {
            return serviceProxy.createSession(session, token);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to create session: " + e.getMessage());
        }
    }

    /**
     * Retrieves the list of sessions for the current user.
     * @return A list of sessions.
     */
    public List<Session> getSessions() {
        try {
            return serviceProxy.getSessions(token);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to retrieve sessions: " + e.getMessage());
        }
    }

}
