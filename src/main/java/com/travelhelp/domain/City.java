package com.travelhelp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class City {
    private long id;
    private String name;
    private float extension;
    private int numberOfHabitants;
    private Country country;

    @Override
    public String toString() {
        return name;
    }
}
