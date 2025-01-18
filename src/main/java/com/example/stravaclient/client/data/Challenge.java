package com.example.stravaclient.client.data;

import java.util.*;

public record Challenge(
        String name,
        String startDate,
        String endDate,
        String goalType,
        Integer goalValue,
        String sport
) {
}

