package com.travelhelp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vaccine {

    private long id;
    private double effectivity;
    private String name;
    private int durability;

    @Override
    public String toString() {
        return name;
    }
}
