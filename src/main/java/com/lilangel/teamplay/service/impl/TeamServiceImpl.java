package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.exception.TeamNotFoundException;
import com.lilangel.teamplay.models.Team;
import com.lilangel.teamplay.repository.TeamRepository;
import com.lilangel.teamplay.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Override
    public Integer create(String name, Integer leadId) {
        Team team = new Team(name, leadId);
        teamRepository.save(team);
        return team.getId();
    }

    @Override
    public void deleteById(Integer id) throws TeamNotFoundException {
        try {
            teamRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new TeamNotFoundException("Team with given ID was not found.");
        }
    }

    @Override
    public Team getById(Integer id) throws TeamNotFoundException {
        Optional<Team> team = teamRepository.findById(id);
        if (team.isPresent()) {
            return team.get();
        } else {
            throw new TeamNotFoundException("Team with given ID was not found.");
        }
    }

    @Override
    public List<Team> getAll() {
        List<Team> teams = new ArrayList<>();
        teamRepository.findAll().forEach(teams::add);
        return teams;
    }
}
