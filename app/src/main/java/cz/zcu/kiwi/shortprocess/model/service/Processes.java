package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cz.zcu.kiwi.shortprocess.model.SQLHelper;

public class Processes extends BaseModelHelper {
    public static final String TABLE = "sp__process";

    public static final String DATE_CREATED = "date_created";
    public static final String NAME = "name"; // todo: rename to "title"
    public static final String DESCRIPTION = "description";

    public Processes(SQLHelper sql) {
        super(sql);
    }

    public void create(String title) {
        SQLiteDatabase db = this.sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_CREATED, System.currentTimeMillis());
        values.put(NAME, title);
        db.insert(TABLE, null, values);
    }

    public Cursor findAll() {
        SQLiteDatabase db = sql.getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null,
                null, null);

        //startManagingCursor(cursor);
        return cursor;
    }
}
