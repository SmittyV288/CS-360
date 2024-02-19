package com.cs360.mattsmithsinventoryappcs360;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InventoryActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener, ProductAdapter.OnItemDeleteClickListener {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private DatabaseHelper db;
    private Switch smsSwitch;
    private static final int PERMISSION_SEND_SMS = 123;
    private static final int LOW_INVENTORY_THRESHOLD = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set up the DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        updateProductList();

        Button addItemButton = findViewById(R.id.btnAddItem);
        addItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(InventoryActivity.this, ProductEditActivity.class);
            startActivity(intent);
        });

        // Initialize the Switch and set up the listener
        smsSwitch = findViewById(R.id.switch1);
        smsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // isChecked will be true if the switch is in the On position
            if (isChecked) {
                checkForSmsPermission();
            }
        });
    }

    private void updateProductList() {
        List<Product> productList = db.getAllProducts();
        if (productAdapter == null) {
            productAdapter = new ProductAdapter(productList, this, this);
            recyclerView.setAdapter(productAdapter);
        } else {
            productAdapter = new ProductAdapter(productList, this, this);
            recyclerView.swapAdapter(productAdapter, false);
        }
    }

    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(InventoryActivity.this, ProductEditActivity.class);
        intent.putExtra("PRODUCT_ID", product.getProductId());
        intent.putExtra("PRODUCT_DESC", product.getProductDesc());
        intent.putExtra("PRODUCT_QUANTITY", product.getQuantity());
        startActivity(intent);
    }

    @Override
    public void onItemDelete(Product product, int position) {
        // Delete the product from the database
        db.deleteProduct(product);
        // Update the adapter
        productAdapter.removeItem(position);

        // After deleting, check if the product quantity is low and send an SMS notification
        if (product.getQuantity() <= LOW_INVENTORY_THRESHOLD) {
            sendLowInventorySms(product);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProductList();
        // Check inventory levels after updating the list
        checkInventoryLevels();
    }

    private void checkInventoryLevels() {
        List<Product> productList = db.getAllProducts();
        for (Product product : productList) {
            if (product.getQuantity() <= LOW_INVENTORY_THRESHOLD) {
                sendLowInventorySms(product);
            }
        }
    }

    private void checkForSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
        } else {
            sendSmsMessage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSmsMessage();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void sendSmsMessage() {
        String phoneNumber = "1234567890"; // Dummy number for testing
        String message = "This is a test SMS message.";

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                Log.d("SMS_TEST", "SMS sent to " + phoneNumber + ": " + message);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sending SMS failed (simulated).", Toast.LENGTH_LONG).show();
                Log.e("SMS_TEST", "SMS sending failed", e);
            }
        } else {
            Log.d("SMS_TEST", "SMS permission not granted.");
            // Permission not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
        }
    }

    private void sendLowInventorySms(Product product) {
        // Only attempt to send an SMS if the switch is on
        if (smsSwitch.isChecked()) {
            String phoneNumber = "1234567890"; //
            String message = "Low inventory alert for " + product.getProductDesc() + "! Only " + product.getQuantity() + " left in stock.";

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
            } else {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "Low inventory alert for " + product.getProductDesc() + "! Only " + product.getQuantity() + " left in stock.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Sending SMS failed.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        } else {
            // The switch is off, do not send SMS
            Toast.makeText(this, "SMS notification is disabled", Toast.LENGTH_SHORT).show();
        }
    }
}
