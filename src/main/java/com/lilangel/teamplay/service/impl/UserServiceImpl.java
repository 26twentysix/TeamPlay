package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.exception.TicketNotFoundException;
import com.lilangel.teamplay.exception.UserNotFoundException;
import com.lilangel.teamplay.models.Ticket;
import com.lilangel.teamplay.models.User;
import com.lilangel.teamplay.repository.TicketRepository;
import com.lilangel.teamplay.repository.UserRepository;
import com.lilangel.teamplay.service.DefaultUserService;
import com.lilangel.teamplay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, DefaultUserService {
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Integer create(Long tgId, Integer employerId, Boolean isAdmin) {
        User user = new User(tgId, employerId, isAdmin);
        userRepository.save(user);
        return user.getId();
    }

    @Override
    public void deleteById(Integer id) throws UserNotFoundException {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public User getById(Integer id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public boolean isAuthorized(Long tgId) {
        return userRepository.existsByTgId(tgId);
    }

    public User getByTgId(Long tgId) {
        return userRepository.getUserByTgId(tgId);
    }


    @Override
    public void changeInfo(Integer ticketId, String... newInfo) throws TicketNotFoundException {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isPresent()) {
            Ticket chosenTicket = ticket.get();
            if (newInfo.length == 2) {
                chosenTicket.setPriority(newInfo[1]);
            }
            chosenTicket.setFullDescription(newInfo[0]);
        } else {
            throw new TicketNotFoundException();
        }
    }

    @Override
    public void pickUpTicket(Integer ticketId, Long tgId) throws UserNotFoundException {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isPresent()) {
            Ticket chosenTicket = ticket.get();
            chosenTicket.setEmployerId(getByTgId(tgId).getEmployerId());
        } else {
            throw new UserNotFoundException();
        }
    }

    @Override
    public void changeTicketState(Integer ticketId, String status) throws TicketNotFoundException {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isPresent()) {
            Ticket chosenTicket = ticket.get();
            chosenTicket.setStatus(status);
        } else {
            throw new TicketNotFoundException();
        }
    }
}
