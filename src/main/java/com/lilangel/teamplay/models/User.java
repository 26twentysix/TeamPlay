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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    private Long tgId;

    private Integer employerId;

    private Boolean isAdmin;

    public User(Long tgId, Integer employerId, Boolean isAdmin) {
        this.tgId = tgId;
        this.employerId = employerId;
        this.isAdmin = isAdmin;
    }
}
