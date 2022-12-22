package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.exception.EmployerNotFoundException;
import com.lilangel.teamplay.models.Employer;
import com.lilangel.teamplay.models.Team;
import com.lilangel.teamplay.repository.EmployerRepository;
import com.lilangel.teamplay.repository.TeamRepository;
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
    private final TeamRepository teamRepository;

    @Autowired
    public EmployerServiceImpl(EmployerRepository employerRepository, TeamRepository teamRepository) {
        this.employerRepository = employerRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public Integer create(String name, String email, Integer teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isPresent()) {
            Employer employer = new Employer(name, email, team.get());
            employerRepository.save(employer);
            return employer.getId();
        }
        return -1;
    }

    @Override
    public void deleteById(Integer id) throws EmployerNotFoundException {
        try {
            employerRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EmployerNotFoundException();
        }
    }

    @Override
    public Employer getById(Integer id) throws EmployerNotFoundException {
        Optional<Employer> employer = employerRepository.findById(id);
        if (employer.isPresent()) {
            return employer.get();
        } else {
            throw new EmployerNotFoundException();
        }
    }

    @Override
    public List<Employer> getAll() {
        List<Employer> employers = new ArrayList<>();
        employerRepository.findAll().forEach(employers::add);
        return employers;
    }
}
