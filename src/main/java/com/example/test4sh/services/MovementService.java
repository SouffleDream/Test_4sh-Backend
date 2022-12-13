package com.example.test4sh.services;

import com.example.test4sh.models.Movement;
import org.springframework.stereotype.Service;

@Service
public class MovementService {

    public Movement create(Movement movement) {
        return movement;
    }
}
