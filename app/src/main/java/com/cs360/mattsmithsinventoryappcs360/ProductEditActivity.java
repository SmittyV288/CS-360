package com.cs360.mattsmithsinventoryappcs360;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ProductEditActivity extends AppCompatActivity {

    private EditText editTextProductId;
    private EditText editTextProductDesc;
    private EditText editTextProductQuantity;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        db = new DatabaseHelper(this);

        editTextProductId = findViewById(R.id.editProductId);
        editTextProductDesc = findViewById(R.id.editProductDescription);
        editTextProductQuantity = findViewById(R.id.editQuantity);


        Button btnSave = findViewById(R.id.btnUpdateItem);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT_ID")) {
            // If PRODUCT_ID is passed, it's an edit operation
            loadProductData(intent);
        }

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String productId = editTextProductId.getText().toString();
        String productDesc = editTextProductDesc.getText().toString();
        int quantity;
        try {
            quantity = Integer.parseInt(editTextProductQuantity.getText().toString());
        } catch (NumberFormatException e) {
            // Handle error where quantity is not an integer
            return;
        }

        Product product = new Product(productId, productDesc, quantity);
        if (db.checkProductExists(productId)) {
            db.updateProduct(product);
        } else {
            db.addProduct(product);
        }

        finish(); // Close the activity and go back to the previous one
    }


    private void loadProductData(Intent intent) {
        // Load product data from the intent and populate the EditText fields
        //Intent intent = getIntent();
        if (intent != null) {
            String productId = intent.getStringExtra("PRODUCT_ID");
            String productDesc = intent.getStringExtra("PRODUCT_DESC");
            int productQuantity = intent.getIntExtra("PRODUCT_QUANTITY", -1); // Default to -1 if not found

            // Populate the fields
            editTextProductId.setText(productId);
            editTextProductDesc.setText(productDesc);
            if (productQuantity != -1) {
                editTextProductQuantity.setText(String.valueOf(productQuantity));
            }
        }
    }

}
