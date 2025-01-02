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

    public Challenge{
        // Defensive copying for mutable collections
        participants = participants != null ? List.copyOf(participants) : List.of();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Challenge that = (Challenge) o;
        return Objects.equals(name, that.name) && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, startDate, endDate, goalType, goalValue, sport);
    }
}

