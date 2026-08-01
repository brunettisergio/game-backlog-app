package com.sergio.backlog_app.model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
@Entity
public class VideoGame extends SoftwareItem {

    @NotBlank(message = "Platform is mandatory")
    private String platform;

    @NotBlank(message = "Status is mandatory")
    private String status; // e.g., Pending, Playing, Finished, Dropped

    private String genre;
    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}
