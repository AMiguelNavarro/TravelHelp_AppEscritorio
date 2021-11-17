package com.travelhelp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CountryDTO {
    private long id;
    private String name;
    private String continent;
    private String flagImage;
    private String acronym;
    private int numberOfHabitants;
    private boolean drinkingWater;
    private boolean publicHealthcare;
    private String prefix;
    private long idCoin;
    private long idElectricity;
}
