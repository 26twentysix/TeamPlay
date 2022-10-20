package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;
import com.lilangel.teamplay.repository.EmployerRepository;
import com.lilangel.teamplay.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public Integer saveNewEmployer(String name, String email, Integer teamId) throws IOException {
        Employer employer = new Employer(name, email, teamId);
        employerRepository.save(employer);
        return employer.getId();
    }

    @Override
    public void deleteById(Integer id) throws EmployerNotFoundException, IOException {
        employerRepository.deleteById(id);
    }

    @Override
    public Employer getById(Integer id) throws EmployerNotFoundException, IOException {
        Optional<Employer> employer = employerRepository.findById(id);
        if (employer.isPresent()) {
            return employer.get();
        } else {
            throw new EmployerNotFoundException("Employer with given ID is not found.");
        }
    }

    @Override
    public List<Employer> getAll() throws IOException {
        List<Employer> employers = new ArrayList<>();
        employerRepository.findAll().forEach(employers::add);
        return employers;
    }
}