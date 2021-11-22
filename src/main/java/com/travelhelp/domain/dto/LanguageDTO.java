package com.travelhelp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class LanguageDTO {
    private String name;
    private boolean official;

    @Override
    public String toString() {
        return name;
    }
}
