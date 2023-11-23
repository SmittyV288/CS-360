package com.zybooks.sayhellobymattsmith;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Define variables
    private EditText nameText;
    private Button buttonSayHello;
    private TextView textGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign variables to views
        nameText = findViewById(R.id.nameText);
        buttonSayHello = findViewById(R.id.buttonSayHello);
        textGreeting = findViewById(R.id.textGreeting);

        // Disable say hello button at start
        buttonSayHello.setEnabled(false);

        // Sets up textwatcher to dynamically enable or disable the sayHello button
        nameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonSayHello.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // Function for when sayHello button is pressed.
    public void SayHello(View v){

        // Checks if name isn't null
        if (nameText.getText() != null){
            String name = nameText.getText().toString();

            // If name text isn't empty, displays hello message with name
            if (!name.isEmpty()) {
                String greeting = "Hello " + name + "!";
                textGreeting.setText(greeting);
            }
            else {
                // Tells user to enter name if name text is empty
                textGreeting.setText("Please enter a name");
            }
        }
    }
}