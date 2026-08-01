package com.sergio.backlog_app.data;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.sergio.backlog_app.model.Game;
import java.util.List;
@Dao
public interface GameDao {
    @Query("SELECT * FROM games ORDER BY title ASC")
    LiveData<List<Game>> getAllGames();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Game> games);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Game game);
    @Delete
    void delete(Game game);
    @Query("DELETE FROM games")
    void deleteAll();
    @Query("SELECT * FROM games WHERE title LIKE :searchQuery")
    LiveData<List<Game>> searchGames(String searchQuery);
}
