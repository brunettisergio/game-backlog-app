package com.sergio.backlog_app.controller;

import com.sergio.backlog_app.model.VideoGame;
import com.sergio.backlog_app.repository.VideoGameRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
public class VideoGameController {

    @Autowired
    private VideoGameRepository repository;

    @GetMapping
    public List<VideoGame> getAllGames() {
        return repository.findAll();
    }

    @PostMapping
    public VideoGame createGame(@Valid @RequestBody VideoGame game) {
        return repository.save(game);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoGame> getGameById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoGame> updateGame(@PathVariable Long id, @Valid @RequestBody VideoGame gameDetails) {
        return repository.findById(id).map(game -> {
            game.setTitle(gameDetails.getTitle());
            game.setDescription(gameDetails.getDescription());
            game.setPlatform(gameDetails.getPlatform());
            game.setStatus(gameDetails.getStatus());
            game.setGenre(gameDetails.getGenre());
            return ResponseEntity.ok(repository.save(game));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGame(@PathVariable Long id) {
        return repository.findById(id).map(game -> {
            repository.delete(game);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<VideoGame> searchGames(@RequestParam String title) {
        return repository.findByTitleContainingIgnoreCase(title);
    }
}
