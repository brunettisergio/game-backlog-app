package com.sergio.backlog_app.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.sergio.backlog_app.data.AppDatabase;
import com.sergio.backlog_app.data.GameDao;
import com.sergio.backlog_app.model.Game;
import com.sergio.backlog_app.network.GameApiService;
import com.sergio.backlog_app.network.RetrofitClient;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRepository {
    private final GameDao gameDao;
    private final GameApiService apiService;
    private final LiveData<List<Game>> allGames;
    private final ExecutorService executorService;

    public GameRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        gameDao = db.gameDao();
        apiService = RetrofitClient.getApiService();
        allGames = gameDao.getAllGames();
        executorService = Executors.newFixedThreadPool(4);
    }

    public LiveData<List<Game>> getAllGames() {
        refreshGames();
        return allGames;
    }

    public void refreshGames() {
        apiService.getAllGames().enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(() -> {
                        gameDao.deleteAll();
                        gameDao.insertAll(response.body());
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {}
        });
    }

    public void insert(Game game) {
        if (game.getId() == null) {
            game.setId(System.currentTimeMillis() * -1);
        }

        executorService.execute(() -> gameDao.insert(game));

        apiService.createGame(game).enqueue(new Callback<Game>() {
            @Override
            public void onResponse(Call<Game> call, Response<Game> response) {
                if (response.isSuccessful() && response.body() != null) {
                    executorService.execute(() -> {
                        gameDao.delete(game);
                        gameDao.insert(response.body());
                    });
                }
            }

            @Override
            public void onFailure(Call<Game> call, Throwable t) {}
        });
    }

    public void delete(Game game) {
        executorService.execute(() -> gameDao.delete(game));

        if (game.getId() != null && game.getId() > 0) {
            apiService.deleteGame(game.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {}

                @Override
                public void onFailure(Call<Void> call, Throwable t) {}
            });
        }
    }

    public LiveData<List<Game>> searchGames(String query) {
        return gameDao.searchGames("%" + query + "%");
    }
}
