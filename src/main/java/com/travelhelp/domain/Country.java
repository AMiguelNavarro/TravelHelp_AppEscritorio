package com.travelhelp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Country {

    private long id;
    private String name;
    private String continent;
    private String flagImage;
    private String acronym;
    private int numberOfHabitants;
    private boolean publicHealthcare;
    private boolean drinkingWater;
    private String prefix;
    private Coin coin;
    private Electricity electricity;

    @Override
    public String toString() {
        return name;
    }
}
