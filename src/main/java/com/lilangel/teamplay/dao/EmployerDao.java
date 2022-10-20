package com.lilangel.teamplay.dao;

import com.lilangel.teamplay.models.Employer;

import java.util.HashMap;


public interface EmployerDao {

    HashMap<Integer, Employer> getAllEmployers();

    void delete(Integer id);

    void add(Employer employer);

    Employer getById(Integer id);
}
