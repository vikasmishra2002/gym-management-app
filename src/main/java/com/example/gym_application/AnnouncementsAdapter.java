package com.example.gym_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.AnnouncementViewHolder> {

    private List<Announcement> announcements = new ArrayList<>();
    Context context;


    public AnnouncementsAdapter(List<Announcement> announcements, Context context) {
        this.announcements = announcements;
        this.context = context;
    }

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_announcement, parent, false);
        return new AnnouncementViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {
        Announcement announcement = announcements.get(position);
        holder.textViewHeading.setText(announcement.getHeading());
        holder.textViewDescription.setText(announcement.getDes());
    }

    @Override
    public int getItemCount() {
        return announcements.size();
    }

    static class AnnouncementViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHeading;
        TextView textViewDescription;

        AnnouncementViewHolder(View itemView) {
            super(itemView);
            textViewHeading = itemView.findViewById(R.id.textViewHeading);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
