package com.lilangel.teamplay.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.criteria.CriteriaBuilder;

@Data
@AllArgsConstructor
public class Employer {
    @Id
    @GeneratedValue
    private Integer id;

    private String email;

    private String name;

    private Integer teamId;

    public Employer(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

}