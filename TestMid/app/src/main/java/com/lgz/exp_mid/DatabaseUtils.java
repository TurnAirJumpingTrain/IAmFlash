package com.lgz.exp_mid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase db;

    private static final String[] columns = {
            DatabaseHelper.ID,
            DatabaseHelper.CONTENT,
            DatabaseHelper.TIME,
            DatabaseHelper.MODE
    };

    public DatabaseUtils(Context context){
        dbHandler = new DatabaseHelper(context);
    }

    public void open(){
        db = dbHandler.getWritableDatabase();
    }

    public void close(){
        dbHandler.close();
    }

    public NotePad addNote(NotePad notePad){
        //add a note object to database
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.CONTENT, notePad.getContent());
        contentValues.put(DatabaseHelper.TIME, notePad.getTime());
        contentValues.put(DatabaseHelper.MODE, notePad.getTag());
        long insertId = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        notePad.setId(insertId);
        return notePad;
    }

    public NotePad getNote(long id){
        //get a note from database using cursor index
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME,columns, DatabaseHelper.ID + "=?",
                new String[]{String.valueOf(id)},null,null, null, null);
        if (cursor != null) cursor.moveToFirst();
        NotePad e = new NotePad(cursor.getString(1),cursor.getString(2), cursor.getInt(3));
        return e;
    }

    public List<NotePad> getAllNotes(){
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME,columns,null,null,null, null, null);

        List<NotePad> notePads = new ArrayList<>();
        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                NotePad notePad = new NotePad();
                notePad.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.ID)));
                notePad.setContent(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CONTENT)));
                notePad.setTime(cursor.getString(cursor.getColumnIndex(DatabaseHelper.TIME)));
                notePad.setTag(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.MODE)));
                notePads.add(notePad);
            }
        }
        return notePads;
    }

    public int updateNote(NotePad notePad) {
        //update the info of an existing note
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CONTENT, notePad.getContent());
        values.put(DatabaseHelper.TIME, notePad.getTime());
        values.put(DatabaseHelper.MODE, notePad.getTag());
        // updating row
        return db.update(DatabaseHelper.TABLE_NAME, values,
                DatabaseHelper.ID + "=?",new String[] { String.valueOf(notePad.getId())});
    }

    public void removeNote(NotePad notePad) {
        //remove a note according to ID value
        db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.ID + "=" + notePad.getId(), null);
    }

}
