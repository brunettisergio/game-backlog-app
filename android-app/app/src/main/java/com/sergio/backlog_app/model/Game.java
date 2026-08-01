package com.sergio.backlog_app.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;
@Entity(tableName = "games")
public class Game implements Serializable {

    @PrimaryKey
    private Long id;
    
    private String title;
    private String description;
    private String platform;
    private String status;
    private String genre;

    // Encapsulation: Private fields with Public Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}
