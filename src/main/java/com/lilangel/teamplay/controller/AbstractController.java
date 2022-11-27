package com.lilangel.teamplay.controller;

import com.lilangel.teamplay.exception.OurNotFoundException;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class AbstractController<T> {
    public abstract ResponseEntity<T> getById(Integer id) throws OurNotFoundException;

    public abstract ResponseEntity<List<T>> getAll();

    public abstract ResponseEntity<?> deleteById(Integer id) throws OurNotFoundException;

}
