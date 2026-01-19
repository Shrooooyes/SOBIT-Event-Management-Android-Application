package com.example.sobit; // change if needed

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    Context context;
    ArrayList<Events> eventList;

    public EventAdapter(Context context, ArrayList<Events> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {

        Events event = eventList.get(position);

        holder.txtEventName.setText(event.eventName);
        holder.txtClient.setText("Client: " + event.clientName);
        holder.txtDate.setText(event.date + "  " + event.time);
        holder.txtStatus.setText(event.status);
        holder.itemView.setOnClickListener(v -> {
            if (event.eventId == null) return;
            Intent intent = new Intent(context, EventDetailsActivity.class);
            intent.putExtra("eventId", event.eventId);
            context.startActivity(intent);
        });

        // Status color
        if (event.status == null) {
            holder.txtStatus.setText("Upcoming");
            holder.txtStatus.setTextColor(Color.BLUE);
        } else if (event.status.equals("Upcoming")) {
            holder.txtStatus.setTextColor(Color.BLUE);
        } else if (event.status.equals("Ongoing")) {
            holder.txtStatus.setTextColor(Color.parseColor("#FFA500"));
        } else if (event.status.equals("Completed")) {
            holder.txtStatus.setTextColor(Color.GREEN);
        } else {
            holder.txtStatus.setTextColor(Color.RED);
        }

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView txtEventName, txtClient, txtDate, txtStatus;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            txtEventName = itemView.findViewById(R.id.txtEventName);
            txtClient = itemView.findViewById(R.id.txtClient);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}
