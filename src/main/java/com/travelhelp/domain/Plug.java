package com.travelhelp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plug {
    private long id;
    private char type;
    private String image;

    @Override
    public String toString() {
        return "Tipo " + type;
    }
}
