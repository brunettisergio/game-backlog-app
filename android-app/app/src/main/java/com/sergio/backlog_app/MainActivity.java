package com.sergio.backlog_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sergio.backlog_app.model.Game;
import com.sergio.backlog_app.ui.GameAdapter;
import com.sergio.backlog_app.ui.ReportActivity;
import com.sergio.backlog_app.viewmodel.GameViewModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GameViewModel gameViewModel;
    private GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new GameAdapter();
        recyclerView.setAdapter(adapter);

        EditText editSearch = findViewById(R.id.edit_search);
        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        gameViewModel.getAllGames().observe(this, games -> {
            String query = editSearch.getText().toString();
            if (query.isEmpty()) {
                adapter.setGames(games);
            } else {
                adapter.setGames(filterGames(games, query));
            }
        });

        adapter.setOnUpdateClickListener(game -> {
            gameViewModel.insert(game); // Updates existing entry
            Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
        });

        adapter.setOnDeleteClickListener(game -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.delete_game)
                    .setMessage("Do you want to delete " + game.getTitle() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        gameViewModel.delete(game);
                        Toast.makeText(this, "Game deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                List<Game> allGames = gameViewModel.getAllGames().getValue();
                if (allGames != null) {
                    if (query.isEmpty()) {
                        adapter.setGames(allGames);
                    } else {
                        adapter.setGames(filterGames(allGames, query));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.fab_add_game).setOnClickListener(v -> showAddGameDialog());
    }

    private List<Game> filterGames(List<Game> games, String query) {
        List<Game> filteredList = new ArrayList<>();
        for (Game g : games) {
            if (g.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(g);
            }
        }
        return filteredList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_report) {
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_game, null);
        builder.setView(dialogView);

        EditText editTitle = dialogView.findViewById(R.id.edit_title);
        EditText editPlatform = dialogView.findViewById(R.id.edit_platform);
        EditText editGenre = dialogView.findViewById(R.id.edit_genre);
        Spinner spinnerStatus = dialogView.findViewById(R.id.spinner_status);

        String[] platforms = {"Steam", "Epic Games", "GOG", "Amazon Games", "PSN", "XBOX"};
        boolean[] selectedPlatforms = new boolean[platforms.length];
        ArrayList<Integer> platformList = new ArrayList<>();

        editPlatform.setOnClickListener(v -> {
            AlertDialog.Builder platBuilder = new AlertDialog.Builder(MainActivity.this);
            platBuilder.setTitle("Select Platforms");
            platBuilder.setCancelable(false);
            platBuilder.setMultiChoiceItems(platforms, selectedPlatforms, (dialogInterface, i, b) -> {
                if (b) {
                    platformList.add(i);
                } else {
                    platformList.remove(Integer.valueOf(i));
                }
            });

            platBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < platformList.size(); j++) {
                    stringBuilder.append(platforms[platformList.get(j)]);
                    if (j != platformList.size() - 1) {
                        stringBuilder.append(", ");
                    }
                }
                editPlatform.setText(stringBuilder.toString());
            });

            platBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            platBuilder.setNeutralButton("Clear All", (dialogInterface, i) -> {
                for (int j = 0; j < selectedPlatforms.length; j++) {
                    selectedPlatforms[j] = false;
                    platformList.clear();
                    editPlatform.setText("");
                }
            });
            platBuilder.show();
        });

        builder.setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String title = editTitle.getText().toString().trim();
            String platform = editPlatform.getText().toString().trim();
            String genre = editGenre.getText().toString();
            String status = spinnerStatus.getSelectedItem().toString();

            boolean isValid = true;
            if (title.isEmpty()) {
                editTitle.setError("Game title is required");
                isValid = false;
            }
            if (platform.isEmpty()) {
                editPlatform.setError("Platform selection is required");
                isValid = false;
            }

            if (isValid) {
                Game game = new Game();
                game.setTitle(title);
                game.setPlatform(platform);
                game.setGenre(genre);
                game.setStatus(status);
                gameViewModel.insert(game);
                Toast.makeText(MainActivity.this, "Game saved", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
