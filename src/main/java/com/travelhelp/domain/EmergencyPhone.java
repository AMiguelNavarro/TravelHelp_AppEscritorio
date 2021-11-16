package com.travelhelp.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmergencyPhone {

    private long id;
    private String phoneNumber;
    private String service;
    private Country country;

    @Override
    public String toString() {
        return service;
    }
}
