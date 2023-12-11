package com.cs360.mattsmithsinventoryappcs360;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "InventoryDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_USERS = "users";


    private static final String KEY_ID = "id";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_PRODUCT_DESC = "product_desc";
    private static final String KEY_QUANTITY = "quantity";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_PHONE = "phone";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PRODUCT_ID + " TEXT,"
                + KEY_PRODUCT_DESC + " TEXT,"
                + KEY_QUANTITY + " INTEGER" + ")";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_USERNAME + " TEXT PRIMARY KEY,"
                + KEY_PASSWORD + " TEXT,"
                + KEY_PHONE + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // CRUD operations (create, read, update, delete)
    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, product.getProductId());
        values.put(KEY_PRODUCT_DESC, product.getProductDesc());
        values.put(KEY_QUANTITY, product.getQuantity());

        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { KEY_ID,
                        KEY_PRODUCT_ID, KEY_PRODUCT_DESC, KEY_QUANTITY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Product product = new Product(
                cursor.getString(1), // Product ID
                cursor.getString(2), // Product Description
                cursor.getInt(3)); // Quantity
        cursor.close();

        return product;
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int productIdIndex = cursor.getColumnIndex(KEY_PRODUCT_ID);
                int productDescIndex = cursor.getColumnIndex(KEY_PRODUCT_DESC);
                int quantityIndex = cursor.getColumnIndex(KEY_QUANTITY);

                if (productIdIndex != -1 && productDescIndex != -1 && quantityIndex != -1) {
                    String productId = cursor.getString(productIdIndex);
                    String productDesc = cursor.getString(productDescIndex);
                    int quantity = cursor.getInt(quantityIndex);

                    Product product = new Product(productId, productDesc, quantity);
                    productList.add(product);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();


        return productList;
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_ID, product.getProductId());
        values.put(KEY_PRODUCT_DESC, product.getProductDesc());
        values.put(KEY_QUANTITY, product.getQuantity());

        // Updating product by product_id
        return db.update(TABLE_PRODUCTS, values, KEY_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getProductId())});
    }
    public boolean checkProductExists(String productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCTS,
                new String[]{KEY_PRODUCT_ID},
                KEY_PRODUCT_ID + "=?",
                new String[]{productId}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, KEY_PRODUCT_ID + " = ?", new String[] { product.getProductId() });
        db.close();
    }

    public void addUser(String username, String password, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_PHONE, phone);
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_USERNAME},
                KEY_USERNAME + "=?", new String[]{username}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkPhoneExists(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_PHONE},
                KEY_PHONE + "=?", new String[]{phone}, null, null, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }


    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_USERNAME, KEY_PASSWORD },
                KEY_USERNAME + "=?", new String[] { username }, null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            String storedPassword = cursor.getString(cursor.getColumnIndex(KEY_PASSWORD));
            cursor.close();
            return password.equals(storedPassword);
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }
}

