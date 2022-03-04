package com.parker.batchlab.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Integer id;

    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String ipAddress;

    public void setGender(String genderType) {
        this.gender = Gender.getByGenderType(genderType);
    }
}
