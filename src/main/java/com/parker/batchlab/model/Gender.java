package com.parker.batchlab.model;

import lombok.Getter;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;

@Getter
public enum Gender {
    MALE("Male"),
    FEMALE("Female");

    private final String genderType;

    Gender(String genderType) {
        this.genderType = genderType;
    }

    public static Gender getByGenderType(final String genderType) {
        return Arrays.stream(Gender.values())
                .filter(gender -> gender.getGenderType().equals(genderType))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
    }
}
