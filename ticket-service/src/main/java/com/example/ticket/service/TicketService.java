package com.example.ticket.service;

import com.example.ticket.dto.TransactionInfo;
import com.example.ticket.model.Ticket;
import com.example.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Ticket> getTicketById(String ticketId) {
        return ticketRepository.findByTicketId(ticketId);
    }

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        if (ticketRepository.findByTicketId(ticket.getTicketId()).isPresent()) {
            throw new RuntimeException("Ticket with ID " + ticket.getTicketId() + " already exists");
        }
        ticket.setStatus("AVAILABLE");
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket updateTicket(String ticketId, Ticket ticketDetails) {
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + ticketId));
        
        ticket.setPrice(ticketDetails.getPrice());
        ticket.setStatus(ticketDetails.getStatus());
        
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteTicket(String ticketId) {
        Ticket ticket = ticketRepository.findByTicketId(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id " + ticketId));
        ticketRepository.delete(ticket);
    }


    @Transactional(readOnly = true)
    public boolean canCommit(TransactionInfo transactionInfo) {
        return ticketRepository.findByTicketId(transactionInfo.getTicketId())
                .map(ticket -> 
                    "AVAILABLE".equals(ticket.getStatus()) && 
                    transactionInfo.getAmount() >= ticket.getPrice()
                )
                .orElse(false);
    }
    
    @Transactional
    public void preCommit(TransactionInfo transactionInfo) {
        Ticket ticket = ticketRepository.findByTicketId(transactionInfo.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus("RESERVED");
        ticketRepository.save(ticket);
    }

    @Transactional
    public void doCommit(TransactionInfo transactionInfo) {
        Ticket ticket = ticketRepository.findByTicketId(transactionInfo.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setStatus("SOLD");
        ticketRepository.save(ticket);
    }

    @Transactional
    public void abort(TransactionInfo transactionInfo) {
        Ticket ticket = ticketRepository.findByTicketId(transactionInfo.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        if ("RESERVED".equals(ticket.getStatus())) {
            ticket.setStatus("AVAILABLE");
            ticketRepository.save(ticket);
        }
    }
}