package com.example.stravaclient.client.swing;

import com.example.stravaclient.client.data.*;
import com.example.stravaclient.client.proxies.IStravaServiceProxy;
import com.example.stravaclient.client.proxies.HttpServiceProxy;

import java.util.HashMap;
import java.util.List;

public class SwingController  {

    private final IStravaServiceProxy serviceProxy = new HttpServiceProxy();
    // Token to be used during the session
    public String token;

        //User management

        public String register(User user, String method, String password){
            return serviceProxy.register(user, method, password);
        }
        public String login(String email, String password){
            return serviceProxy.login(email, password);
        }

        public String logout(String token){
            return serviceProxy.logout(token);
        }

        // Challenge management


        public String createChallenge(Challenge challenge, String token){
            return serviceProxy.createChallenge(challenge, token);
        }
        public List<Challenge> getActiveChallenges(){
            return serviceProxy.getActiveChallenges();
        }
        public String acceptChallenge(String token, int id){
            return serviceProxy.acceptChallenge(token, id);
        }
        public HashMap<Integer, Double> getChallengeStatus(String token){
            return serviceProxy.getChallengeStatus(token);
        }

        // Session management

        public String createSession(Session session, String token){
            return serviceProxy.createSession(session, token);
        }
        public List<Session> getSessions(String token){
            return serviceProxy.getSessions(token);
        }
        public User getInformation(String token){
            return serviceProxy.getInfo(token);
        }
}