package com.sergio.backlog_app.repository;

import com.sergio.backlog_app.model.VideoGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoGameRepository extends JpaRepository<VideoGame, Long> {
    List<VideoGame> findByTitleContainingIgnoreCase(String title);
    List<VideoGame> findByStatus(String status);
    List<VideoGame> findByPlatform(String platform);
}
