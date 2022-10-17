package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.models.Employer;
import com.lilangel.teamplay.service.EmployerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employers")
public class EmployerController {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String TEAM_ID = "team_id";

    private final EmployerService employerService;

    @Autowired
    public EmployerController(EmployerService employerService) {
        this.employerService = employerService;
    }

    @GetMapping(value = "/get?{id}")
    public ResponseEntity<Employer> getById(@PathVariable Integer id) {
        Employer employer = employerService.getById();
        return new ResponseEntity<>(employer, HttpStatus.OK);
    }

    @GetMapping(value = "/getall")
    public ResponseEntity<List<Employer>> getAll() {
        List<Employer> allEmployers = employerService.getAll();
        return new ResponseEntity<>(allEmployers, HttpStatus.OK);

    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> create(@RequestParam(ID) Integer id,
                                    @RequestParam(NAME) String name,
                                    @RequestParam(EMAIL) String email,
                                    @RequestParam(TEAM_ID) Integer teamId) {
        Integer createdId = employerService.createNewEmployer(id, name, email, teamId);
        return new ResponseEntity<>(createdId.toString(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete?{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        employerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
