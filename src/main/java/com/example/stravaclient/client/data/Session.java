package com.example.stravaclient.client.data;



public record Session(
        Integer id,
        User user,
        String title,
        String sport,
        Double distance,
        String startDate,
        String startTime,
        String duration
) {
}

