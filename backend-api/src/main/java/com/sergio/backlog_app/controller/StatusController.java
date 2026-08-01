package com.sergio.backlog_app.controller;

import com.sergio.backlog_app.model.Status;
import com.sergio.backlog_app.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statuses")
@CrossOrigin(origins = "*")
public class StatusController {

    @Autowired
    private StatusRepository repository;

    @GetMapping
    public List<Status> getAllStatuses() {
        return repository.findAll();
    }

    @PostMapping
    public Status createStatus(@RequestBody Status status) {
        return repository.save(status);
    }
}
