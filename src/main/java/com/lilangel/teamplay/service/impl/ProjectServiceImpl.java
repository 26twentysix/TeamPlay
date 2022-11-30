package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.exception.ProjectNotFoundException;
import com.lilangel.teamplay.models.Project;
import com.lilangel.teamplay.repository.ProjectRepository;
import com.lilangel.teamplay.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Integer create(String name, String description, Integer teamId) {
        Project project = new Project(name, description, teamId);
        projectRepository.save(project);
        return project.getId();
    }

    @Override
    public void deleteById(Integer id) throws ProjectNotFoundException {
        try {
            projectRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ProjectNotFoundException();
        }
    }

    @Override
    public Project getById(Integer id) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isPresent()) {
            return project.get();
        } else {
            throw new ProjectNotFoundException();
        }
    }

    @Override
    public List<Project> getAll() {
        List<Project> projects = new ArrayList<>();
        projectRepository.findAll().forEach(projects::add);
        return projects;
    }
}
