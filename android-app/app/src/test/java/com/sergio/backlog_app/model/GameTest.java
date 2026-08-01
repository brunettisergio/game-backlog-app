package com.sergio.backlog_app.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void testGameEncapsulation() {
        Game game = new Game();
        game.setTitle("Elden Ring");
        game.setPlatform("PS5");
        game.setStatus("PLAYING");

        assertEquals("Elden Ring", game.getTitle());
        assertEquals("PS5", game.getPlatform());
        assertEquals("PLAYING", game.getStatus());
    }

    @Test
    public void testGameId() {
        Game game = new Game();
        game.setId(100L);
        assertEquals(Long.valueOf(100), game.getId());
    }
}
