package com.cs360.mattsmithsinventoryappcs360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editPassword;
    private DatabaseHelper db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);

        // Initialize the buttons
        Button loginButton = findViewById(R.id.btnLogin);
        Button registerButton = findViewById(R.id.btnRegister);
        db = new DatabaseHelper(this);

        // Set OnClickListener for login button
        loginButton.setOnClickListener(v -> {

            String username = editUsername.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            // Intent to start Login Activity
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_LONG).show();
                return;
            }

            if (db.validateUser(username, password)) {
                Intent intent = new Intent(LoginActivity.this, InventoryActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
            }


        });

        // Set OnClickListener for register button
        registerButton.setOnClickListener(v -> {
            // Intent to start Register Activity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }


}