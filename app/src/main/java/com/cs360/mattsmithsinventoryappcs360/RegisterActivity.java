package com.cs360.mattsmithsinventoryappcs360;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editRegisterUser;
    private EditText editRegisterPassword;
    private EditText editRegisterPhone;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editRegisterUser = findViewById(R.id.editRegisterUser);
        editRegisterPassword = findViewById(R.id.editRegisterPassword);
        editRegisterPhone = findViewById(R.id.editRegisterPhone);
        Button registerButton = findViewById(R.id.btnSubmitRegister);
        db = new DatabaseHelper(this);

        registerButton.setOnClickListener(v -> {
            String username = editRegisterUser.getText().toString().trim();
            String password = editRegisterPassword.getText().toString().trim();
            String phone = editRegisterPhone.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                return;
            }

            if (db.checkUsernameExists(username)) {
                Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_LONG).show();
                return;
            }

            if (db.checkPhoneExists(phone)) {
                Toast.makeText(RegisterActivity.this, "Phone number already registered", Toast.LENGTH_LONG).show();
                return;
            }

            db.addUser(username, password, phone);
            Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
