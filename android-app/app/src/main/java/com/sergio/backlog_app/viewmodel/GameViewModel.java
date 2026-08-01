package com.sergio.backlog_app.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.sergio.backlog_app.model.Game;
import com.sergio.backlog_app.repository.GameRepository;
import java.util.List;

public class GameViewModel extends AndroidViewModel {
    private final GameRepository repository;
    private final LiveData<List<Game>> allGames;

    public GameViewModel(@NonNull Application application) {
        super(application);
        repository = new GameRepository(application);
        allGames = repository.getAllGames();
    }

    public LiveData<List<Game>> getAllGames() {
        return allGames;
    }

    public void insert(Game game) {
        repository.insert(game);
    }

    public void delete(Game game) {
        repository.delete(game);
    }

    public LiveData<List<Game>> searchGames(String query) {
        return repository.searchGames(query);
    }
}
