package com.sergio.backlog_app.network;

import com.sergio.backlog_app.model.Game;
import com.sergio.backlog_app.model.Status;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;
public interface GameApiService {
    @GET("api/games")
    Call<List<Game>> getAllGames();

    @POST("api/games")
    Call<Game> createGame(@Body Game game);

    @PUT("api/games/{id}")
    Call<Game> updateGame(@Path("id") Long id, @Body Game game);

    @DELETE("api/games/{id}")
    Call<Void> deleteGame(@Path("id") Long id);

    @GET("api/games/search")
    Call<List<Game>> searchGames(@Query("title") String title);

    @GET("api/statuses")
    Call<List<Status>> getAllStatuses();
}
