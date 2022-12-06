package com.lilangel.teamplay.exception;

public class TicketNotFoundException extends OurNotFoundException {
    public TicketNotFoundException() {
            super("Ticket with given ID was not found.");
        }
}
