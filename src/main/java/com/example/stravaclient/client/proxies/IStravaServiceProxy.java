package com.example.stravaclient.client.proxies;

import com.example.stravaclient.client.data.*;
import java.util.HashMap;
import java.util.List;

public interface IStravaServiceProxy {

    //User management

    String register(User user, String method, String password);
    String login(String email, String method, String password);
    String logout(String token);


    // Challenge management


    String createChallenge(Challenge challenge, String token);
    List<Challenge> getActiveChallenges();
    String acceptChallenge(String token, int id);
    HashMap<String, String> getChallengeStatus(String token);

    // Session management

    String createSession(Session session, String token);
    List<Session> getSessions(String token);
}
