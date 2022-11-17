package com.lilangel.teamplay.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String description;

    private Integer teamId;

    public Project(String name, String description, Integer teamId) {
        this.name = name;
        this.description = description;
        this.teamId = teamId;
    }
}
