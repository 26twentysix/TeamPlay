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
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer projectId;

    private String priority;

    private String status;

    private String shortDescription;

    private String fullDescription;

    private Integer employerId;

    public Ticket(Integer projectId, String priority, String status, String shortDescription, String fullDescription, Integer employerId) {
        this.priority = priority;
        this.projectId = projectId;
        this.status = status;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.employerId = employerId;
    }
}
