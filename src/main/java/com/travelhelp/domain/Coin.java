package com.travelhelp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Coin {
    private long id;
    private String isoCode;
    private String symbol;
    private String monetaryUnit;

    @Override
    public String toString() {
        return monetaryUnit;
    }
}
