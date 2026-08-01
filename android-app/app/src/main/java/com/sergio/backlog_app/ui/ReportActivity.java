package com.sergio.backlog_app.ui;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.sergio.backlog_app.R;
import com.sergio.backlog_app.model.Game;
import com.sergio.backlog_app.viewmodel.GameViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_report);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        TextView textTimestamp = findViewById(R.id.text_timestamp);
        TextView textTotal = findViewById(R.id.text_total_games);
        TextView textFinished = findViewById(R.id.text_finished_games);
        TextView textCompletion = findViewById(R.id.text_completion_rate);
        TableLayout tableReport = findViewById(R.id.table_report);

        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        textTimestamp.setText(String.format("Generated on: %s", currentDateTime));

        GameViewModel viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        viewModel.getAllGames().observe(this, games -> {
            updateStats(games, textTotal, textFinished, textCompletion);
            populateTable(games, tableReport);
        });
    }

    private void updateStats(List<Game> games, TextView total, TextView finished, TextView completion) {
        int totalCount = games.size();
        long finishedCount = 0;
        for (Game g : games) {
            if ("FINISHED".equalsIgnoreCase(g.getStatus())) {
                finishedCount++;
            }
        }
        total.setText(String.valueOf(totalCount));
        finished.setText(String.valueOf(finishedCount));

        if (totalCount > 0) {
            double rate = (double) finishedCount / totalCount * 100;
            completion.setText(String.format(Locale.getDefault(), "%.1f%%", rate));
        } else {
            completion.setText("0%");
        }
    }

    private void populateTable(List<Game> games, TableLayout table) {
        table.removeAllViews();
        
        TableRow header = new TableRow(this);
        header.addView(createCell("Title", true));
        header.addView(createCell("Platform", true));
        header.addView(createCell("Status", true));
        table.addView(header);

        for (Game game : games) {
            TableRow row = new TableRow(this);
            row.addView(createCell(game.getTitle(), false));
            row.addView(createCell(game.getPlatform(), false));
            row.addView(createCell(game.getStatus(), false));
            table.addView(row);
        }
    }

    private TextView createCell(String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        if (isHeader) {
            textView.setTypeface(null, android.graphics.Typeface.BOLD);
            textView.setBackgroundColor(getColor(R.color.dark_surface));
            textView.setTextColor(getColor(R.color.primary_accent));
        } else {
            textView.setTextColor(getColor(R.color.text_primary));
        }
        return textView;
    }
}
