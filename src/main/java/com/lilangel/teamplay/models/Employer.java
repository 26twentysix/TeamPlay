package com.lilangel.teamplay.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employers")
public class Employer {
    @Id
    @GeneratedValue
    private Integer id;

    private String email;

    private String name;

    private Integer teamId;

    public Employer(String name, String email, Integer teamId) {
        this.name = name;
        this.email = email;
        this.teamId = teamId;
    }
}