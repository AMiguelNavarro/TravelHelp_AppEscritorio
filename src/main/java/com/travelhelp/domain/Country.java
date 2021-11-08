package com.travelhelp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Country {

    private String name;
    private String continent;
    private String flagImage;
    private String acronym;
    private int numberOfHabitants;
    private boolean publicHealthcare;
    private boolean drinkingWater;
    private String prefix;

    @Override
    public String toString() {
        return name;
    }
}