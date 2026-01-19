package com.example.sobit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDetailsActivity extends AppCompatActivity {

    TextView txtEventName, txtClient, txtPhone, txtVenue, txtType;
    TextView txtDateTime, txtPayment, txtStatus, txtAmount;
    Button btnEdit, btnDelete, btnWhatsapp;
    String eventId;
    String clientPhone, clientName, eventName, date, time;


    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        txtEventName = findViewById(R.id.txtEventName);
        txtClient = findViewById(R.id.txtClient);
        txtPhone = findViewById(R.id.txtPhone);
        txtVenue = findViewById(R.id.txtVenue);
        txtType = findViewById(R.id.txtType);
        txtDateTime = findViewById(R.id.txtDateTime);
        txtPayment = findViewById(R.id.txtPayment);
        txtStatus = findViewById(R.id.txtStatus);
        txtAmount = findViewById(R.id.txtAmount);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnWhatsapp = findViewById(R.id.btnWhatsapp);

        String eventId = getIntent().getStringExtra("eventId");

        if (eventId == null) {
            finish();
            return;
        }
        database = FirebaseDatabase.getInstance()
                .getReference("events")
                .child(eventId);

        btnDelete.setOnClickListener(v -> deleteEvent());
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditEventActivity.class);
            intent.putExtra("eventId", eventId);
            startActivity(intent);
        });
        btnWhatsapp.setOnClickListener(v -> {

            String phone = "91" + clientPhone; // India country code

            String message = "Hello " + clientName +
                    ",\nYour event \"" + eventName +
                    "\" is scheduled on " + date +
                    " at " + time + ".";

            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://wa.me/" + phone + "?text=" +
                                Uri.encode(message)
                ));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
            }
        });


        loadEventDetails();
    }

    private void loadEventDetails() {

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Events event = snapshot.getValue(Events.class);
                if (event == null) return;

                txtEventName.setText(event.eventName);
                txtClient.setText("Client: " + event.clientName);
                txtPhone.setText("Phone: " + event.phone);
                txtVenue.setText("Venue: " + event.venue);
                txtType.setText("Type: " + event.type);
                txtDateTime.setText("Date & Time: " + event.date + " " + event.time);
                txtStatus.setText("Status: " + event.status);
                txtPayment.setText("Payment: " + event.paymentStatus);

                txtAmount.setText(
                        "Total: ₹" + event.totalAmount +
                                "\nAdvance: ₹" + event.advance +
                                "\nRemaining: ₹" + event.remaining
                );
                clientPhone = event.phone;
                clientName = event.clientName;
                eventName = event.eventName;
                date = event.date;
                time = event.time;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void deleteEvent() {

        new AlertDialog.Builder(this)
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete this event?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    database.removeValue().addOnSuccessListener(unused -> {
                        finish(); // go back after delete
                    });

                })
                .setNegativeButton("No", null)
                .show();
    }

}
