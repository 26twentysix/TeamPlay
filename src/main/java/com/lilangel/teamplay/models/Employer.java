package com.lilangel.teamplay.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employer {

    private Integer id;

    private String email;

    private String name;

    private Integer teamId;

}
