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
    public Session {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (sport == null || sport.isBlank()) {
            throw new IllegalArgumentException("Sport cannot be null or blank");
        }
        if (distance == null || distance < 0) {
            throw new IllegalArgumentException("Distance cannot be null or negative");
        }
        if (startDate == null || startDate.isBlank()) {
            throw new IllegalArgumentException("Start date cannot be null or blank");
        }
        if (startTime == null || startTime.isBlank()) {
            throw new IllegalArgumentException("Start time cannot be null or blank");
        }
        if (duration == null || duration.isBlank()) {
            throw new IllegalArgumentException("Duration cannot be null or blank");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session that = (Session) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, sport, distance, startDate, startTime, duration);
    }
}

