package com.example.stravaclient.client.data;

public record Challenge(
        Integer id,
        String name,
        String startDate,
        String endDate,
        String goalType,
        Integer goalValue,
        String sport
) {
}

