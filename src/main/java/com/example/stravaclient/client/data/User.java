package com.example.stravaclient.client.data;


import java.util.*;

public record User(
        String email,
        String name,
        String birthdate,
        Double weight,
        Double height,
        Integer maxHeartRate,
        Integer restHeartRate
) {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User that = (User) o;
        return Objects.equals(email, that.email) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name);
    }
}

