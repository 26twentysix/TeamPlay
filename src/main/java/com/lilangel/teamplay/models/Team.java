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
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @OneToOne
    private Employer lead;

    public Team(String name, Employer lead) {
        this.name = name;
        this.lead = lead;
    }

    public Integer getLeadId() {
        return lead.getId();
    }
}
