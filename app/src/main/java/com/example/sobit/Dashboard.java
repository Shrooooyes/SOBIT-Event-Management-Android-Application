package com.example.sobit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    TextView txtTotalEvents, txtUpcoming, txtCompleted, txtRevenue;
    Button btnAdd, btnView;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtTotalEvents = findViewById(R.id.txtTotalEvents);
        txtUpcoming = findViewById(R.id.txtUpcoming);
        txtCompleted = findViewById(R.id.txtCompleted);
        txtRevenue = findViewById(R.id.txtRevenue);

        database = FirebaseDatabase.getInstance().getReference("events");

        btnAdd = findViewById(R.id.btnAddEvent);
        btnView = findViewById(R.id.btnViewEvents);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddEventActivity.class)));

        btnView.setOnClickListener(v ->
                startActivity(new Intent(this, EventListActivity.class)));

        loadDashboardData();
    }

    private void loadDashboardData() {

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalEvents = 0;
                int upcomingEvents = 0;
                int completedEvents = 0;
                int totalRevenue = 0;

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    Events event = dataSnapshot.getValue(Events.class);
//                    System.out.println(event);
                    if (event == null) continue;

                    totalEvents++;

                    // Status count (NULL SAFE)
                    if (event.status == null || event.status.equals("Upcoming")) {
                        upcomingEvents++;
                    } else if (event.status.equals("Completed")) {
                        completedEvents++;
                    }

                    // Revenue (only PAID or PARTIAL)
                    if (event.advance > 0) {
                        totalRevenue += event.advance;
                    }
                }

                txtTotalEvents.setText("Total Events: " + totalEvents);
                txtUpcoming.setText("Upcoming Events: " + upcomingEvents);
                txtCompleted.setText("Completed Events: " + completedEvents);
                txtRevenue.setText("Total Revenue: ₹" + totalRevenue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}