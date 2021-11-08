package com.travelhelp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Electricity {

    private int voltage;
    private int frecuency;

    @Override
    public String toString() {
        return "Voltaje -> " + voltage + " || Frecuencia -> " + frecuency;
    }
}
