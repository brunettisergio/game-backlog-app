package com.sergio.backlog_app.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.sergio.backlog_app.R;
import com.sergio.backlog_app.model.Game;
import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameHolder> {
    private List<Game> games = new ArrayList<>();
    private OnItemClickListener listener;
    private int expandedPosition = -1;
    private OnDeleteClickListener deleteClickListener;
    private OnUpdateClickListener updateClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(Game game);
    }

    public interface OnUpdateClickListener {
        void onUpdateClick(Game game);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.updateClickListener = listener;
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game, parent, false);
        return new GameHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        Game currentGame = games.get(position);
        holder.textViewTitle.setText(currentGame.getTitle());
        holder.textViewPlatform.setText("Platforms: " + currentGame.getPlatform());
        holder.textViewStatus.setText(currentGame.getStatus());
        
        holder.editTitle.setText(currentGame.getTitle() != null ? currentGame.getTitle() : "");
        holder.editPlatform.setText(currentGame.getPlatform() != null ? currentGame.getPlatform() : "");
        holder.editGenre.setText(currentGame.getGenre() != null ? currentGame.getGenre() : "");
        holder.textViewDescription.setText("Description: " + (currentGame.getDescription() != null ? currentGame.getDescription() : "None"));

        int color;
        switch (currentGame.getStatus().toUpperCase()) {
            case "FINISHED": color = holder.itemView.getContext().getColor(R.color.status_finished); break;
            case "PLAYING": color = holder.itemView.getContext().getColor(R.color.status_playing); break;
            case "PENDING": color = holder.itemView.getContext().getColor(R.color.status_pending); break;
            case "DROPPED": color = holder.itemView.getContext().getColor(R.color.status_dropped); break;
            default: color = Color.GRAY;
        }
        
        GradientDrawable background = (GradientDrawable) holder.textViewStatus.getBackground();
        background.setColor(color);

        final boolean isExpanded = position == expandedPosition;
        holder.layoutExpanded.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);

        if (isExpanded) {
            String[] statusArray = holder.itemView.getContext().getResources().getStringArray(R.array.game_status_array);
            for (int i = 0; i < statusArray.length; i++) {
                if (statusArray[i].equals(currentGame.getStatus())) {
                    holder.spinnerStatus.setSelection(i);
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public void setGames(List<Game> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    class GameHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewPlatform;
        private final TextView textViewStatus;
        private final EditText editTitle;
        private final EditText editPlatform;
        private final EditText editGenre;
        private final TextView textViewDescription;
        private final LinearLayout layoutExpanded;
        private final Spinner spinnerStatus;

        public GameHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_game_title);
            textViewPlatform = itemView.findViewById(R.id.text_game_platform);
            textViewStatus = itemView.findViewById(R.id.text_game_status);
            editTitle = itemView.findViewById(R.id.edit_game_title);
            editPlatform = itemView.findViewById(R.id.edit_game_platform);
            editGenre = itemView.findViewById(R.id.edit_game_genre);
            textViewDescription = itemView.findViewById(R.id.text_game_description);
            layoutExpanded = itemView.findViewById(R.id.layout_expanded_info);
            spinnerStatus = itemView.findViewById(R.id.spinner_edit_status);
            Button btnDelete = itemView.findViewById(R.id.btn_delete_game);
            Button btnSave = itemView.findViewById(R.id.btn_save_changes);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    int previousExpanded = expandedPosition;
                    expandedPosition = (expandedPosition == position) ? -1 : position;
                    
                    notifyItemChanged(previousExpanded);
                    notifyItemChanged(position);
                    
                    if (listener != null) {
                        listener.onItemClick(games.get(position));
                    }
                }
            });

            editPlatform.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                String[] platforms = {"Steam", "Epic Games", "GOG", "Amazon Games", "PSN", "XBOX"};
                String currentPlatforms = editPlatform.getText().toString();
                boolean[] selectedPlatforms = new boolean[platforms.length];
                ArrayList<Integer> platformList = new ArrayList<>();

                for (int i = 0; i < platforms.length; i++) {
                    if (currentPlatforms.contains(platforms[i])) {
                        selectedPlatforms[i] = true;
                        platformList.add(i);
                    }
                }

                AlertDialog.Builder platBuilder = new AlertDialog.Builder(itemView.getContext(), R.style.CustomDialogTheme);
                platBuilder.setTitle("Edit Platforms");
                platBuilder.setMultiChoiceItems(platforms, selectedPlatforms, (dialogInterface, i, b) -> {
                    if (b) {
                        if (!platformList.contains(i)) platformList.add(i);
                    } else {
                        platformList.remove(Integer.valueOf(i));
                    }
                });

                platBuilder.setPositiveButton("OK", (dialogInterface, i) -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int j = 0; j < platformList.size(); j++) {
                        stringBuilder.append(platforms[platformList.get(j)]);
                        if (j != platformList.size() - 1) stringBuilder.append(", ");
                    }
                    editPlatform.setText(stringBuilder.toString());
                });
                platBuilder.setNegativeButton("Cancel", null);
                platBuilder.show();
            });

            btnSave.setOnClickListener(v -> {
                int adapterPos = getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    Game game = games.get(adapterPos);
                    game.setTitle(editTitle.getText().toString());
                    game.setPlatform(editPlatform.getText().toString());
                    game.setGenre(editGenre.getText().toString());
                    game.setStatus(spinnerStatus.getSelectedItem().toString());
                    
                    if (updateClickListener != null) {
                        updateClickListener.onUpdateClick(game);
                    }
                    
                    // Collapse after save
                    expandedPosition = -1;
                    notifyItemChanged(adapterPos);
                }
            });

            btnDelete.setOnClickListener(v -> {
                int adapterPos = getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    if (deleteClickListener != null) {
                        deleteClickListener.onDeleteClick(games.get(adapterPos));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Game game);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
