package com.lilangel.teamplay.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.dialect.function.DB2SubstringFunction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;

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

    private Integer projectID;

    private String priority;

    private String shortDescription;

    private String fullDescription;

    private Integer employerId;

    public Ticket(Integer projectID, String priority,
                  String shortDescription, String fullDescription, Integer employerId) {
        this.priority = priority;
        this.projectID = projectID;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.employerId = employerId;
    }
}
