package com.example.sobit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {

    EditText edtEventName, edtClientName, edtPhone, edtVenue;
    EditText edtDate, edtTime, edtTotal, edtAdvance;
    TextView txtRemaining;
    Spinner spinnerType;
    Button btnSaveEvent;

    DatabaseReference database;
    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_event);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔹 View Binding
        edtEventName = findViewById(R.id.edtEventName);
        edtClientName = findViewById(R.id.edtClientName);
        edtPhone = findViewById(R.id.edtPhone);
        edtVenue = findViewById(R.id.edtVenue);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtTotal = findViewById(R.id.edtTotal);
        edtAdvance = findViewById(R.id.edtAdvance);
        txtRemaining = findViewById(R.id.txtRemaining);
        spinnerType = findViewById(R.id.spinnerType);
        btnSaveEvent = findViewById(R.id.btnSaveEvent);

        // 🔹 Firebase
        database = FirebaseDatabase.getInstance().getReference("events");
        eventId = getIntent().getStringExtra("eventId");

        if (eventId == null) {
            Toast.makeText(this, "Invalid Event", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadEventData();

        // 🔹 Date Picker
        edtDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(
                    this,
                    (view, year, month, day) ->
                            edtDate.setText(MessageFormat.format("{0}/{1}/{2}", day, month + 1, year)),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // 🔹 Time Picker
        edtTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(
                    this,
                    (view, hour, minute) ->
                            edtTime.setText(String.format("%02d:%02d", hour, minute)),
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    true
            ).show();
        });

        // 🔹 Auto Remaining Calculation
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                calculateRemaining();
            }
        };

        edtTotal.addTextChangedListener(watcher);
        edtAdvance.addTextChangedListener(watcher);

        // 🔹 Save Button
        btnSaveEvent.setOnClickListener(v -> updateEvent());
    }

    // 🔹 Calculate Remaining Amount
    private void calculateRemaining() {
        int total = edtTotal.getText().toString().isEmpty() ? 0 :
                Integer.parseInt(edtTotal.getText().toString());

        int advance = edtAdvance.getText().toString().isEmpty() ? 0 :
                Integer.parseInt(edtAdvance.getText().toString());

        txtRemaining.setText("Remaining: ₹" + (total - advance));
    }

    // 🔹 Load Existing Event
    private void loadEventData() {
        database.child(eventId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Events event = snapshot.getValue(Events.class);
                        if (event == null) return;

                        edtEventName.setText(event.eventName);
                        edtClientName.setText(event.clientName);
                        edtPhone.setText(event.phone);
                        edtVenue.setText(event.venue);
                        edtDate.setText(event.date);
                        edtTime.setText(event.time);
                        edtTotal.setText(String.valueOf(event.totalAmount));
                        edtAdvance.setText(String.valueOf(event.advance));
                        txtRemaining.setText("Remaining: ₹" + event.remaining);

                        ArrayAdapter adapter = (ArrayAdapter) spinnerType.getAdapter();
                        spinnerType.setSelection(adapter.getPosition(event.type));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(EditEventActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // 🔹 Update Event in Firebase
    private void updateEvent() {
        if (edtEventName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Event Name", Toast.LENGTH_SHORT).show();
            return;
        }

        int total = edtTotal.getText().toString().isEmpty() ? 0 :
                Integer.parseInt(edtTotal.getText().toString());
        int advance = edtAdvance.getText().toString().isEmpty() ? 0 :
                Integer.parseInt(edtAdvance.getText().toString());

        Events event = new Events();
        event.eventName = edtEventName.getText().toString();
        event.clientName = edtClientName.getText().toString();
        event.phone = edtPhone.getText().toString();
        event.venue = edtVenue.getText().toString();
        event.type = spinnerType.getSelectedItem().toString();
        event.date = edtDate.getText().toString();
        event.time = edtTime.getText().toString();
        event.totalAmount = total;
        event.advance = advance;
        event.remaining = total - advance;
        event.status = "Upcoming";

        if (advance == 0)
            event.paymentStatus = "Pending";
        else if (advance < total)
            event.paymentStatus = "Partial";
        else
            event.paymentStatus = "Cleared";

        database.child(eventId).setValue(event)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Event Updated Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                );
    }
}
