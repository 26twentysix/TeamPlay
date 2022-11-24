package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.exception.TicketNotFoundException;
import com.lilangel.teamplay.models.Ticket;
import com.lilangel.teamplay.repository.TicketRepository;
import com.lilangel.teamplay.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Integer saveNewTicket(Integer projectId, String priority, String status, String shortDescription,
                                 String fullDescription, Integer employerId) {
        Ticket ticket = new Ticket(projectId, priority, status, shortDescription, fullDescription, employerId);
        ticketRepository.save(ticket);
        return ticket.getId();
    }

    @Override
    public void deleteById(Integer id) throws TicketNotFoundException {
        try {
            ticketRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new TicketNotFoundException("Ticket with given ID was not found.");
        }
    }

    @Override
    public Ticket getById(Integer id) throws TicketNotFoundException {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            return ticket.get();
        } else {
            throw new TicketNotFoundException("Ticket with given ID was not found.");
        }
    }

    @Override
    public List<Ticket> getAll() {
        List<Ticket> tickets = new ArrayList<>();
        ticketRepository.findAll().forEach(tickets::add);
        return tickets;
    }
}
