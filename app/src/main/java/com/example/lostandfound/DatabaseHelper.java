package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LostAndFoundNative.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_CAMPUSES = "campuses";
    public static final String COL_CAMPUS_ID = "id";
    public static final String COL_CAMPUS_CODE = "campus_code";
    public static final String COL_CAMPUS_NAME = "campus_name";

    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_PHONE = "phone";
    public static final String COL_USER_CAMPUS_CODE = "campus_code";

    public static final String TABLE_ITEMS = "items";
    public static final String COL_ITEM_ID = "id";
    public static final String COL_ITEM_CAMPUS = "campus_code";
    public static final String COL_ITEM_USER_NAME = "user_name";
    public static final String COL_ITEM_USER_EMAIL = "user_email";
    public static final String COL_ITEM_TYPE = "type";
    public static final String COL_ITEM_IMAGE_URI = "image_uri";
    public static final String COL_ITEM_DESCRIPTION = "description";
    public static final String COL_ITEM_LOCATION = "location";
    public static final String COL_ITEM_DATE = "date";
    public static final String COL_ITEM_PHONE = "phone";

    private static final String SQL_CREATE_CAMPUSES =
            "CREATE TABLE " + TABLE_CAMPUSES + " (" +
                    COL_CAMPUS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_CAMPUS_CODE + " TEXT UNIQUE, " +
                    COL_CAMPUS_NAME + " TEXT);";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USER_NAME + " TEXT, " +
                    COL_USER_EMAIL + " TEXT UNIQUE, " +
                    COL_USER_PASSWORD + " TEXT, " +
                    COL_USER_PHONE + " TEXT, " +
                    COL_USER_CAMPUS_CODE + " TEXT);";

    private static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + TABLE_ITEMS + " (" +
                    COL_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_ITEM_CAMPUS + " TEXT, " +
                    COL_ITEM_USER_NAME + " TEXT, " +
                    COL_ITEM_USER_EMAIL + " TEXT, " +
                    COL_ITEM_TYPE + " TEXT, " +
                    COL_ITEM_IMAGE_URI + " TEXT, " +
                    COL_ITEM_DESCRIPTION + " TEXT, " +
                    COL_ITEM_LOCATION + " TEXT, " +
                    COL_ITEM_DATE + " TEXT, " +
                    COL_ITEM_PHONE + " TEXT);";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CAMPUSES);
        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_ITEMS);
        seedInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAMPUSES);
        onCreate(db);
    }

    private void seedInitialData(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            insertCampusInternal(db, "STAN2026", "Stanford University");
            insertUserInternal(db, "Alex Rivera", "alex@stanford.edu", "1234", "9876543210", "STAN2026");
            insertUserInternal(db, "Emma Watson", "emma@stanford.edu", "1234", "9876543211", "STAN2026");

            insertItemInternal(db, "STAN2026", "Alex Rivera", "alex@stanford.edu", "lost",
                    "android.resource://com.example.lostandfound/drawable/mackbook",
                    "Lost a space grey MacBook Pro 14 inch in a black sleeve near computer lab desks.",
                    "Gates Computer Science Bldg", "26 Sep 2026", "9876543210");

            insertItemInternal(db, "STAN2026", "Emma Watson", "emma@stanford.edu", "lost",
                    "android.resource://com.example.lostandfound/drawable/headphone",
                    "Lost blue Sony WH-1000XM4 noise cancelling headphones in leather case.",
                    "Green Library 2nd Floor", "25 Sep 2026", "9876543211");

            insertItemInternal(db, "STAN2026", "Alex Rivera", "alex@stanford.edu", "found",
                    "android.resource://com.example.lostandfound/drawable/honda_key",
                    "Found a set of Honda car keys with red Stanford lanyard on table 4.",
                    "Tressider Student Union", "27 Sep 2026", "9876543210");

            insertItemInternal(db, "STAN2026", "Emma Watson", "emma@stanford.edu", "found",
                    "android.resource://com.example.lostandfound/drawable/apple_watch",
                    "Found titanium Apple Watch with magnetic loop strap beside bench.",
                    "Main Quad Garden", "27 Sep 2026", "9876543211");

            insertCampusInternal(db, "HARV2026", "Harvard University");
            insertUserInternal(db, "David Kim", "david@harvard.edu", "1234", "9123456780", "HARV2026");
            insertUserInternal(db, "Sophia Martinez", "sophia@harvard.edu", "1234", "9123456781", "HARV2026");

            insertItemInternal(db, "HARV2026", "David Kim", "david@harvard.edu", "lost",
                    "android.resource://com.example.lostandfound/drawable/wallet",
                    "Lost brown leather Fossil wallet containing Student ID & CharlieCard transit pass.",
                    "Widener Library Steps", "26 Sep 2026", "9123456780");

            insertItemInternal(db, "HARV2026", "Sophia Martinez", "sophia@harvard.edu", "lost",
                    "android.resource://com.example.lostandfound/drawable/bottle",
                    "Lost stainless steel Hydro Flask (black, 32oz) with robotics team stickers.",
                    "Science & Engineering Complex", "25 Sep 2026", "9123456781");

            insertItemInternal(db, "HARV2026", "David Kim", "david@harvard.edu", "found",
                    "android.resource://com.example.lostandfound/drawable/glasses",
                    "Found black Ray-Ban prescription eyeglasses in a brown hard case on sofa.",
                    "Annenberg Dining Hall", "27 Sep 2026", "9123456780");

            insertItemInternal(db, "HARV2026", "Sophia Martinez", "sophia@harvard.edu", "found",
                    "android.resource://com.example.lostandfound/drawable/calculator",
                    "Found Casio FX-991CW scientific calculator on row 3 desk.",
                    "Sever Hall Room 102", "27 Sep 2026", "9123456781");

            insertUserInternal(db, "Test User", "test@test.com", "test", "9998887770", "STAN2026");

            insertItemInternal(db, "STAN2026", "Test User", "test@test.com", "lost",
                    "android.resource://com.example.lostandfound/drawable/bottle",
                    "Lost my blue insulated water bottle near the swimming pool locker room.",
                    "Avery Aquatic Center", "28 Sep 2026", "9998887770");

            insertItemInternal(db, "STAN2026", "Test User", "test@test.com", "found",
                    "android.resource://com.example.lostandfound/drawable/calculator",
                    "Found a scientific calculator left behind after midterms.",
                    "Building 320 Room 105", "28 Sep 2026", "9998887770");

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void insertCampusInternal(SQLiteDatabase db, String code, String name) {
        ContentValues cv = new ContentValues();
        cv.put(COL_CAMPUS_CODE, code);
        cv.put(COL_CAMPUS_NAME, name);
        db.insert(TABLE_CAMPUSES, null, cv);
    }

    private void insertUserInternal(SQLiteDatabase db, String name, String email, String password, String phone, String campusCode) {
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USER_EMAIL, email.trim().toLowerCase(Locale.ROOT));
        cv.put(COL_USER_PASSWORD, password);
        cv.put(COL_USER_PHONE, phone);
        cv.put(COL_USER_CAMPUS_CODE, campusCode);
        db.insert(TABLE_USERS, null, cv);
    }

    private void insertItemInternal(SQLiteDatabase db, String campusCode, String userName, String userEmail, String type,
                                    String imageUri, String description, String location, String date, String phone) {
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEM_CAMPUS, campusCode);
        cv.put(COL_ITEM_USER_NAME, userName);
        cv.put(COL_ITEM_USER_EMAIL, userEmail != null ? userEmail.trim().toLowerCase(Locale.ROOT) : "");
        cv.put(COL_ITEM_TYPE, type);
        cv.put(COL_ITEM_IMAGE_URI, imageUri);
        cv.put(COL_ITEM_DESCRIPTION, description);
        cv.put(COL_ITEM_LOCATION, location);
        cv.put(COL_ITEM_DATE, date);
        cv.put(COL_ITEM_PHONE, phone);
        db.insert(TABLE_ITEMS, null, cv);
    }

    public boolean createCampus(String code, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CAMPUS_CODE, code.trim().toUpperCase(Locale.ROOT));
        cv.put(COL_CAMPUS_NAME, name.trim());
        long result = db.insertWithOnConflict(TABLE_CAMPUSES, null, cv, SQLiteDatabase.CONFLICT_IGNORE);
        return result != -1;
    }

    public boolean campusExists(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CAMPUSES, new String[]{COL_CAMPUS_CODE},
                COL_CAMPUS_CODE + " = ?", new String[]{code.trim().toUpperCase(Locale.ROOT)},
                null, null, null);

        boolean exists = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return exists;
    }

    public long registerUser(String name, String email, String password, String phone, String campusCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER_NAME, name);
        cv.put(COL_USER_EMAIL, email.trim().toLowerCase(Locale.ROOT));
        cv.put(COL_USER_PASSWORD, password);
        cv.put(COL_USER_PHONE, phone);
        cv.put(COL_USER_CAMPUS_CODE, campusCode != null ? campusCode.trim().toUpperCase(Locale.ROOT) : "");
        return db.insert(TABLE_USERS, null, cv);
    }

    public boolean validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null,
                COL_USER_EMAIL + " = ? AND " + COL_USER_PASSWORD + " = ?",
                new String[]{email.trim().toLowerCase(Locale.ROOT), password},
                null, null, null);
        boolean valid = (cursor != null && cursor.getCount() > 0);
        if (cursor != null) cursor.close();
        return valid;
    }

    public String getUserNameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_NAME},
                COL_USER_EMAIL + " = ?", new String[]{email.trim().toLowerCase(Locale.ROOT)},
                null, null, null);
        String name = "";
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME));
            }
            cursor.close();
        }
        return name;
    }

    public void insertItem(String campusCode, String userName, String userEmail, String type, String imageUri,
                           String description, String location, String date, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COL_ITEM_CAMPUS, campusCode);
            values.put(COL_ITEM_USER_NAME, userName);
            values.put(COL_ITEM_USER_EMAIL, userEmail != null ? userEmail.trim().toLowerCase(Locale.ROOT) : "");
            values.put(COL_ITEM_TYPE, type);
            values.put(COL_ITEM_IMAGE_URI, imageUri);
            values.put(COL_ITEM_DESCRIPTION, description);
            values.put(COL_ITEM_LOCATION, location);
            values.put(COL_ITEM_DATE, date);
            values.put(COL_ITEM_PHONE, phone);

            db.insert(TABLE_ITEMS, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public boolean deleteItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_ITEMS, COL_ITEM_ID + " = ?", new String[]{String.valueOf(itemId)});
        return rows > 0;
    }

    public List<ItemModel> getItemsByCampusAndType(String campusCode, String type) {
        List<ItemModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = COL_ITEM_TYPE + " = ? AND (" + COL_ITEM_CAMPUS + " = ? OR " + COL_ITEM_CAMPUS + " IS NULL)";
        String[] selectionArgs = new String[]{type, campusCode};

        Cursor cursor = db.query(TABLE_ITEMS, null, selection, selectionArgs, null, null, COL_ITEM_ID + " DESC");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(extractItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }

    public List<ItemModel> getAllItemsByCampus(String campusCode) {
        List<ItemModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = "(" + COL_ITEM_CAMPUS + " = ? OR " + COL_ITEM_CAMPUS + " IS NULL)";
        String[] selectionArgs = new String[]{campusCode};

        Cursor cursor = db.query(TABLE_ITEMS, null, selection, selectionArgs, null, null, COL_ITEM_ID + " DESC");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(extractItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }

    public List<ItemModel> getItemsByUserEmail(String email) {
        List<ItemModel> list = new ArrayList<>();
        if (email == null || email.trim().isEmpty()) {
            return list;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COL_ITEM_USER_EMAIL + " = ?";
        String[] selectionArgs = new String[]{email.trim().toLowerCase(Locale.ROOT)};

        Cursor cursor = db.query(TABLE_ITEMS, null, selection, selectionArgs, null, null, COL_ITEM_ID + " DESC");
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        list.add(extractItemFromCursor(cursor));
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }

    private ItemModel extractItemFromCursor(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ITEM_ID));
        String uriStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_IMAGE_URI));
        String desc = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_DESCRIPTION));
        String loc = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_LOCATION));
        String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_DATE));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_PHONE));
        String postedBy = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_USER_NAME));
        String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_USER_EMAIL));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(COL_ITEM_TYPE));

        Uri uri = (uriStr != null && !uriStr.isEmpty()) ? Uri.parse(uriStr) : null;
        ItemModel item = new ItemModel(uri, desc, loc, date, phone);
        item.setId(id);
        item.setPostedBy(postedBy);
        item.setUserEmail(userEmail);
        item.setType(type);
        return item;
    }
}
