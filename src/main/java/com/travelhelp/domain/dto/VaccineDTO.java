package com.travelhelp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineDTO {
    private double effectivity;
    private int durability;
    private boolean obligatory;
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
