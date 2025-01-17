package com.example.stravaclient.client.data;

import java.util.Objects;

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

