package com.example.sobit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.MessageFormat;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    EditText edtEventName, edtClientName, edtPhone, edtVenue;
    EditText edtDate, edtTime, edtTotal, edtAdvance;
    TextView txtRemaining;
    Spinner spinnerType;
    Button btnSaveEvent;

    DatabaseReference database;

    int totalAmount = 0;
    int advanceAmount = 0;


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

        database = FirebaseDatabase.getInstance().getReference("events");

        edtDate.setOnClickListener(v-> {
            Calendar c = Calendar.getInstance();

            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, year, month, day) ->
                                edtDate.setText(MessageFormat.format("{0}/{1}/{2}", day, month + 1, year)),
                    c.get(Calendar.YEAR),
                    c.get(Calendar.MONTH),
                    c.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        edtTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();

            TimePickerDialog dialog = new TimePickerDialog(
                    this,
                    (view, hour, minute) ->
                            edtTime.setText(MessageFormat.format("{0}:{1}", hour<10? "0"+hour : hour, minute<10? "0"+minute : minute )),
                    c.get(Calendar.HOUR_OF_DAY),
                    c.get(Calendar.MINUTE),
                    true
            );
            dialog.show();
        });

        edtAdvance.addTextChangedListener((new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {}

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!edtTotal.getText().toString().isEmpty()){
                    totalAmount = Integer.parseInt(edtTotal.getText().toString());
                }

                if(edtAdvance.getText().toString().isEmpty()){
                    advanceAmount = Integer.parseInt(edtAdvance.getText().toString());
                }

                int remaining = totalAmount - advanceAmount;
                txtRemaining.setText("Remaining: ₹" + remaining);
            }
        }));

        btnSaveEvent.setOnClickListener(v -> {
            if(edtEventName.getText().toString().isEmpty()){
                Toast.makeText(this, "Enter Event Nam", Toast.LENGTH_SHORT).show();
                return;
            }

            Events event = new Events();
            event.eventName = edtEventName.getText().toString();
            event.clientName = edtClientName.getText().toString();
            event.phone = edtPhone.getText().toString();
            event.venue = edtVenue.getText().toString();
            event.type = spinnerType.getSelectedItem().toString();
            event.date = edtDate.getText().toString();
            event.time = edtTime.getText().toString();
            event.totalAmount = totalAmount;
            event.advance = advanceAmount;
            event.remaining = totalAmount - advanceAmount;
            event.status = "Upcoming";

            if(advanceAmount == 0)
                    event.paymentStatus = "Pending";
            else if (advanceAmount < totalAmount)
                event.paymentStatus = "Partial";
            else
                event.paymentStatus = "Cleared";

            String eventId = database.push().getKey();
            event.eventId = eventId;

            database.child(eventId).setValue(event)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Event Added Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to add Event", Toast.LENGTH_SHORT).show();
                        finish();
                    });

        });
    }
}