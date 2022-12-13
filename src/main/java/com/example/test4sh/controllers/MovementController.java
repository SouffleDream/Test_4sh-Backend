package com.example.test4sh.controllers;

import com.example.test4sh.models.Movement;
import com.example.test4sh.repository.MovementRepository;
import com.example.test4sh.services.MailSenderService;
import com.example.test4sh.services.MovementService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movements")
class MovementController {
    private final MailSenderService mailSender;
    private final MovementRepository repository;
    private final MovementService movementService;

    MovementController(MovementRepository repository, MailSenderService mailSender, MovementService movementService) {
        this.repository = repository;
        this.mailSender = mailSender;
        this.movementService = movementService;
    }

    @GetMapping("")
    List<Movement> all() {
        return repository.findAll();
    }

    @PostMapping("/{typeMovement}&{emailTo}")
    Movement newMovement(@Validated @RequestBody Movement newMovement, @PathVariable(value = "typeMovement") String typeMovement, @PathVariable(value = "emailTo") String emailTo) throws Exception {
        Movement movementCreated = movementService.create(newMovement);
        mailSender.SendMovement(typeMovement, movementCreated, emailTo);
        return repository.save(newMovement);
    }
}
