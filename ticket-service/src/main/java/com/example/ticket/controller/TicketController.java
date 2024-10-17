package com.example.ticket.controller;

import com.example.ticket.dto.TransactionInfo;
import com.example.ticket.model.Ticket;
import com.example.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable String ticketId) {
        return ticketService.getTicketById(ticketId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket ticket) throws RuntimeException  {
        Ticket ticketRespose = ticketService.createTicket(ticket);
        return ResponseEntity.ok(ticketRespose);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<Ticket> updateTicket(@PathVariable String ticketId, @RequestBody Ticket ticketDetails) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketId, ticketDetails));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable String ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok().build();
    }    

    @PostMapping("/canCommit")
    public ResponseEntity<Boolean> canCommit(@RequestBody TransactionInfo transactionInfo) {
        boolean canCommit = ticketService.canCommit(transactionInfo);
        return ResponseEntity.ok(canCommit);
    }

    @PostMapping("/preCommit")
    public ResponseEntity<Void> preCommit(@RequestBody TransactionInfo transactionInfo) {
        ticketService.preCommit(transactionInfo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/doCommit")
    public ResponseEntity<Void> doCommit(@RequestBody TransactionInfo transactionInfo) {
        ticketService.doCommit(transactionInfo);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/abort")
    public ResponseEntity<Void> abort(@RequestBody TransactionInfo transactionInfo) {
        ticketService.abort(transactionInfo);
        return ResponseEntity.ok().build();
    }
}