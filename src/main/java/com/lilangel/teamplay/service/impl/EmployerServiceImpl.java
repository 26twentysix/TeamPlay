package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;
import com.lilangel.teamplay.repository.EmployerRepository;
import com.lilangel.teamplay.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployerServiceImpl implements EmployerService {
    private final EmployerRepository employerRepository;

    @Autowired
    public EmployerServiceImpl(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    @Override
    public Integer create(String name, String email, Integer teamId) {
        Employer employer = new Employer(name, email, teamId);
        employerRepository.save(employer);
        return employer.getId();
    }

    @Override
    public void deleteById(Integer id) throws EmployerNotFoundException {
        try {
            employerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EmployerNotFoundException("Employer with given ID was not found.");
        }
    }

    @Override
    public Employer getById(Integer id) throws EmployerNotFoundException {
        Optional<Employer> employer = employerRepository.findById(id);
        if (employer.isPresent()) {
            return employer.get();
        } else {
            throw new EmployerNotFoundException("Employer with given ID was not found.");
        }
    }

    @Override
    public List<Employer> getAll() {
        List<Employer> employers = new ArrayList<>();
        employerRepository.findAll().forEach(employers::add);
        return employers;
    }
}
