package com.example.databasetest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.chrono.MinguoChronology;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView textView1, textView2;
    private EditText inputUsername, inputStatus;
    private Button buttonUpdate;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUsername = findViewById(R.id.username_input);
        inputStatus = findViewById(R.id.status_input);
        buttonUpdate = findViewById(R.id.button_update);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String status = inputStatus.getText().toString().trim();

                if (username.isEmpty() || status.isEmpty()) {
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("username", username);
                        hashMap.put("status", status);
                    databaseReference.child("Users").child(username).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Data saved", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    databaseReference.child("Users").child(username).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String retrieveUsername = snapshot.child("username").getValue().toString();
                            String retrieveStatus = snapshot.child("status").getValue().toString();

                            textView1.setText(retrieveUsername);
                            textView2.setText(retrieveStatus);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}