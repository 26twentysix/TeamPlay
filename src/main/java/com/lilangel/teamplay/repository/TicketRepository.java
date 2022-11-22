package com.lilangel.teamplay.repository;

import com.lilangel.teamplay.models.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Integer> {
}
