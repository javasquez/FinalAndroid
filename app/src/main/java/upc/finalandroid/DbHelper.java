package upc.finalandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "PTDMA";
    private static final int DB_VER = 1;
    public static final String DB_TABLE = "Task";
    public static final String DB_COLUMN = "TaskName";

    public static final String DB_TABLE_QUERY = "Queries";
    public static final String DB_COLUMN_QUERY = "LastQuery";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL);", DB_TABLE, DB_COLUMN);
        db.execSQL(query);
        query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT NOT NULL);", DB_TABLE_QUERY, DB_COLUMN_QUERY);
        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DELETE TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        query = String.format("DELETE TABLE IF EXISTS %s", DB_TABLE_QUERY);
        db.execSQL(query);
        onCreate(db);

    }

    public void insertNewTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN, task.toLowerCase());
        db.insertWithOnConflict(DB_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void insertLastQuert(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN_QUERY, query);
        db.insertWithOnConflict(DB_TABLE_QUERY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteLastQuery(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE_QUERY, DB_COLUMN_QUERY + " = ?", new String[]{query});
        db.close();
    }

    public void deleteTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, DB_COLUMN + " = ?", new String[]{task.toLowerCase()});
        db.close();
    }

    public ArrayList<String> getTaskList() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DB_COLUMN);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public ArrayList<String> getLastQuery() {
        ArrayList<String> lastQueries = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE_QUERY, new String[]{DB_COLUMN_QUERY}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DB_COLUMN_QUERY);
            lastQueries.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return lastQueries;
    }

}