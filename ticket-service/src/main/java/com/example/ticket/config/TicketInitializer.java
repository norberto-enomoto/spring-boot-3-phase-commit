package com.example.ticket.config;

import com.example.ticket.model.Ticket;
import com.example.ticket.repository.TicketRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class TicketInitializer {

    @Bean
    public CommandLineRunner initializeTickets(TicketRepository ticketRepository) {
        return args -> {
            if (ticketRepository.count() == 0) {
                for (int i = 0; i < 10; i++) {
                    Ticket ticket = new Ticket();
                    ticket.setTicketId(UUID.randomUUID().toString());
                    ticket.setStatus("AVAILABLE");
                    ticket.setPrice(100.0);
                    ticketRepository.save(ticket);
                }
            }
        };
    }
}




