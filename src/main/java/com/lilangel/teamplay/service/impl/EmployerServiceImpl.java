package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.dao.EmployerDao;
import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;
import com.lilangel.teamplay.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

@Service
public class EmployerServiceImpl implements EmployerService {
    private final EmployerDao employerDao;

    @Autowired
    public EmployerServiceImpl(EmployerDao employerDao) {
        this.employerDao = employerDao;
    }

    @Override
    public Integer createNewEmployer(String name, String email, Integer teamId) throws IOException {
        Employer employer = new Employer(name, email, teamId);
        Integer id = employer.getId();
        employerDao.add(employer);
        return id;
    }

    @Override
    public void deleteById(Integer id) throws EmployerNotFoundException, IOException {
        employerDao.delete(id);
    }

    @Override
    public Employer getById(Integer id) throws EmployerNotFoundException, IOException {
        return employerDao.getById(id);
    }

    @Override
    public HashMap<Integer, Employer> getAll() throws IOException {
        return employerDao.getAllEmployers();
    }
}