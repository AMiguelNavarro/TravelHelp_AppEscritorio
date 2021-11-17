package com.travelhelp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class CityDTO {
    private long id;
    private String name;
    private float extension;
    private int numberOfHabitants;
    private long idCountry;
}
