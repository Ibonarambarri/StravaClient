package com.example.stravaclient.client.data;



public record User(
        Integer id,
        String email,
        String name,
        String birthdate,
        Double weight,
        Double height,
        Integer maxHeartRate,
        Integer restHeartRate
) { }

