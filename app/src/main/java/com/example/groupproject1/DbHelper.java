package com.example.groupproject1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "friendsapp.db";
    private static final int DATABASE_VERSION = 1;

    // Table: USER
    private static final String TABLE_USER = "USER";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    // Table: FRIENDS
    private static final String TABLE_FRIENDS = "FRIENDS";
    private static final String KEY_FRIENDS_ID = "friendsid";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_FNUMBER = "fnumber";
    private static final String KEY_FAGE = "fage";
    private static final String KEY_FDOB = "fdob";
    private static final String KEY_FUSER_ID = "userid"; // FK from USER

    // Table: MESSAGE
    private static final String TABLE_MESSAGE = "MESSAGE";
    private static final String KEY_MESSAGE_ID = "messageid";
    private static final String KEY_MESSAGE_TEXT = "messagetext";
    private static final String KEY_MESSAGE_DATE = "messagedate";
    private static final String KEY_MESSAGE_USER_ID = "userid"; // FK from USER
    private static final String KEY_MESSAGE_FRIEND_ID = "friendsid"; // FK from FRIENDS

    // Table: TODOLIST
    private static final String TABLE_TODOLIST = "TODOLIST";
    private static final String KEY_TODO_ID = "todoid";
    private static final String KEY_TODO_DATE = "todo_date";
    private static final String KEY_TODO_TEXT = "todo_text";
    private static final String KEY_TODO_USER_ID = "userid"; // FK from USER

    SQLiteDatabase db;

    // Constructor
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creates for all 4 tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create for USER table
        String createUserTable = "CREATE TABLE " + TABLE_USER + " (" +
                KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_USERNAME + " TEXT, " +
                KEY_NAME + " TEXT, " +
                KEY_EMAIL + " TEXT, " +
                KEY_PASSWORD + " TEXT)";

        // Create for FRIENDS table
        String createFriendsTable = "CREATE TABLE " + TABLE_FRIENDS + " (" +
                KEY_FRIENDS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_FNAME + " TEXT, " +
                KEY_FNUMBER + " TEXT, " +
                KEY_FAGE + " INTEGER, " +
                KEY_FDOB + " TEXT, " +
                KEY_FUSER_ID + " INTEGER, " +
                "FOREIGN KEY(" + KEY_FUSER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_USER_ID + "))";

        // Create for MESSAGE table
        String createMessageTable = "CREATE TABLE " + TABLE_MESSAGE + " (" +
                KEY_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_MESSAGE_TEXT + " TEXT, " +
                KEY_MESSAGE_DATE + " TEXT, " +
                KEY_MESSAGE_USER_ID + " INTEGER, " +
                KEY_MESSAGE_FRIEND_ID + " INTEGER, " +
                "FOREIGN KEY(" + KEY_MESSAGE_USER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_USER_ID + "), " +
                "FOREIGN KEY(" + KEY_MESSAGE_FRIEND_ID + ") REFERENCES " + TABLE_FRIENDS + "(" + KEY_FRIENDS_ID + "))";

        // Create for TODOLIST table
        String createTodoTable = "CREATE TABLE " + TABLE_TODOLIST + " (" +
                KEY_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TODO_DATE + " TEXT, " +
                KEY_TODO_TEXT + " TEXT, " +
                KEY_TODO_USER_ID + " INTEGER, " +
                "FOREIGN KEY(" + KEY_TODO_USER_ID + ") REFERENCES " + TABLE_USER + "(" + KEY_USER_ID + "))";

        // Execute all SQL statements to make sure it create the table
        db.execSQL(createUserTable);
        db.execSQL(createFriendsTable);
        db.execSQL(createMessageTable);
        db.execSQL(createTodoTable);
    }

    // Upgrades all the 4 tables
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    // Insert into USER table (untuk masukkan data user (from login))
    public void insertUser(String username, String name, String email, String password) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    // Insert into FRIENDS table (untuk masukkan data friends (from add friends))
    public void insertFriend(String fname, String fnumber, int fage, String fdob, int userid) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FNAME, fname);
        values.put(KEY_FNUMBER, fnumber);
        values.put(KEY_FAGE, fage);
        values.put(KEY_FDOB, fdob);
        values.put(KEY_FUSER_ID, userid);
        db.insert(TABLE_FRIENDS, null, values);
        db.close();
    }

    // Insert into MESSAGE table (untuk create message and set bila nak send)
    public void insertMessage(String text, String date, int userid, int friendsid) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_MESSAGE_TEXT, text);
        values.put(KEY_MESSAGE_DATE, date);
        values.put(KEY_MESSAGE_USER_ID, userid);
        values.put(KEY_MESSAGE_FRIEND_ID, friendsid);
        db.insert(TABLE_MESSAGE, null, values);
        db.close();
    }

    // Insert into TODOLIST table (ni untuk listkan apa yang dia nak buat)
    public void insertTodo(String date, String text, int userid) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TODO_DATE, date);
        values.put(KEY_TODO_TEXT, text);
        values.put(KEY_TODO_USER_ID, userid);
        db.insert(TABLE_TODOLIST, null, values);
        db.close();
    }

    // Example: Get all TODOs for a specific user
    public Cursor getTodosByUser(int userid) {
        db = this.getReadableDatabase();
        return db.query(TABLE_TODOLIST, null, KEY_TODO_USER_ID + "=?", new String[]{String.valueOf(userid)}, null, null, null);
    }

    // Example: Get all messages between a user and friend
    public Cursor getMessages(int userid, int friendsid) {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_MESSAGE +
                " WHERE " + KEY_MESSAGE_USER_ID + "=? AND " + KEY_MESSAGE_FRIEND_ID + "=?";
        return db.rawQuery(query, new String[]{String.valueOf(userid), String.valueOf(friendsid)});
    }

    // Delete a friend by ID
    public void deleteFriend(int friendId) {
        db = this.getWritableDatabase();
        db.delete(TABLE_FRIENDS, KEY_FRIENDS_ID + "=?", new String[]{String.valueOf(friendId)});
        db.close();
    }

    // Update user info
    public void updateUser(int userid, String username, String name, String email, String password) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_PASSWORD, password);
        db.update(TABLE_USER, values, KEY_USER_ID + "=?", new String[]{String.valueOf(userid)});
        db.close();
    }
}
