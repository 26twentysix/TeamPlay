package com.lilangel.teamplay.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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

    @OneToOne
    private Team team;

    public Project(String name, String description, Team team) {
        this.name = name;
        this.description = description;
        this.team = team;
    }

    public Integer getTeamId() {
        return team.getId();
    }
}
