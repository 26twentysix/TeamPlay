package com.lilangel.teamplay.models;

import lombok.*;

import javax.persistence.*;

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

    @OneToOne
    private Team team;

    public Employer(String name, String email, Team team) {
        this.name = name;
        this.email = email;
        this.team = team;
    }

    public Integer getTeamId() {
        return team.getId();
    }
}