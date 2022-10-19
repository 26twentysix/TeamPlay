package com.lilangel.teamplay.dao.impl;

import com.lilangel.teamplay.dao.EmployerDao;
import com.lilangel.teamplay.models.Employer;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EmployerDaoImpl implements EmployerDao {

    private final HashMap<Integer, Employer> employers;
    private static Integer currentId;

    public EmployerDaoImpl() {
        this.employers = new HashMap<>();
        currentId = 0;
    }

    @Override
    public HashMap<Integer, Employer> getAllEmployers() {
        return employers;
    }

    @Override
    public void delete(Integer id) {
        employers.remove(id);
    }

    @Override
    public void add(Employer employer) {
        employer.setId(currentId++);
        employers.put(employer.getId(), employer);
    }

    @Override
    public Employer getById(Integer id) {
        return employers.get(id);
    }
}
