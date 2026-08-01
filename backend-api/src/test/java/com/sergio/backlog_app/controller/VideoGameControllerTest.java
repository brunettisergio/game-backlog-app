package com.sergio.backlog_app.controller;

import com.sergio.backlog_app.model.VideoGame;
import com.sergio.backlog_app.repository.StatusRepository;
import com.sergio.backlog_app.repository.VideoGameRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VideoGameController.class)
public class VideoGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoGameRepository repository;

    @MockBean
    private StatusRepository statusRepository; // Mocked to satisfy BacklogApplication dependency

    @Test
    public void testGetAllGames() throws Exception {
        VideoGame game = new VideoGame();
        game.setTitle("Test Game");
        game.setPlatform("PC");
        game.setStatus("PENDING");

        Mockito.when(repository.findAll()).thenReturn(Arrays.asList(game));

        mockMvc.perform(get("/api/games")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Game"))
                .andExpect(jsonPath("$[0].platform").value("PC"));
    }
}
