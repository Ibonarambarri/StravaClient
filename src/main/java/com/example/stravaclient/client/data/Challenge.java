package com.example.stravaclient.client.data;

import java.util.*;

public record Challenge(
        int id,
        User owner,
        List<User> participants,
        String name,
        String startDate,
        String endDate,
        String goalType,
        Integer goalValue,
        String sport
) {
}

