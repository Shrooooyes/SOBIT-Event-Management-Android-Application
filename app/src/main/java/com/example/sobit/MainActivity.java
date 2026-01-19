package com.example.sobit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText emailEt, passEt;
    Button loginBt;
    ProgressBar progressBar;

     public void getUser(String email, String password){
         auth.signInWithEmailAndPassword(email, password)
                 .addOnCompleteListener(this, task -> {
//                     progressBar.setVisibility(View.VISIBLE);
                     if (task.isSuccessful()) {
                         FirebaseUser user = auth.getCurrentUser();
                         Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
//                         progressBar.setVisibility(View.GONE);

                         Intent intent = new Intent(MainActivity.this, Dashboard.class);
                         startActivity(intent);
                     } else {
                         Toast.makeText(this,
                                 "Login failed: " + Objects.requireNonNull(task.getException()).getMessage(),
                                 Toast.LENGTH_LONG).show();
                     }
                 });
     }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FirebaseMessaging.getInstance().subscribeToTopic("events");
        emailEt = (EditText) findViewById(R.id.edtEmail);
        passEt = (EditText) findViewById(R.id.edtPassword);
        loginBt = findViewById(R.id.btnLogin);
//        progressBar = findViewById(R.id.progressBar);

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEt.getText().toString();
                String password = passEt.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    getUser(email,password);
                }
                else{
                    if(email.isEmpty()){
                        emailEt.setError("Enter Email ID");
                    }
                    if(password.isEmpty()){
                        passEt.setError("Enter Password");
                    }
                }

            }
        });
    }
}