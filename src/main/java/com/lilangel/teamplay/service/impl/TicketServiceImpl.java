package com.lilangel.teamplay.service.impl;

import com.lilangel.teamplay.exception.TicketNotFoundException;
import com.lilangel.teamplay.models.Ticket;
import com.lilangel.teamplay.repository.TicketRepository;
import com.lilangel.teamplay.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Integer create(Integer projectId, String priority, String status, String shortDescription,
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
            throw new TicketNotFoundException();
        }
    }

    @Override
    public Ticket getById(Integer id) throws TicketNotFoundException {
        Optional<Ticket> ticket = ticketRepository.findById(id);
        if (ticket.isPresent()) {
            return ticket.get();
        } else {
            throw new TicketNotFoundException();
        }
    }

    @Override
    public List<Ticket> getAll() {
        List<Ticket> tickets = new ArrayList<>();
        ticketRepository.findAll().forEach(tickets::add);
        return tickets;
    }

    @Override
    public Ticket updateTicketInfo(Integer ticketId, Map<String, String> args) throws TicketNotFoundException {
        Optional<Ticket> ticket = ticketRepository.findById(ticketId);
        if (ticket.isPresent()) {
            Ticket chosenTicket = ticket.get();
            Map<String, Consumer<String>> map = new HashMap<>();
            map.put("priority", chosenTicket::setPriority);
            map.put("full_description", chosenTicket::setFullDescription);
            map.put("short_description", chosenTicket::setShortDescription);
            map.put("status", chosenTicket::setStatus);
            for (String command : map.keySet()) {
                if (args.get(command) != null) {
                    map.get(command).accept(args.get(command));
                }
            }
            if (args.get("employer_id") != null) {
                chosenTicket.setEmployerId(Integer.parseInt(args.get("employer_id")));
            }
            ticketRepository.save(chosenTicket);
            return chosenTicket;
        } else {
            throw new TicketNotFoundException();
        }
    }
}
