package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParsePosition;
import java.util.Date;

import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;
import cz.zcu.kiwi.shortprocess.model.entity.Process;

public class Processes extends BaseModelHelper {
    public static final String TABLE = "sp__process";

    public static final String DATE_CREATED = "date_created";
    public static final String TITLE = "name"; // todo: rename to "title"
    public static final String DESCRIPTION = "description";

    private final ModelCursor.Parser parser = new ModelCursor.Parser() {
        @Override
        public Process parse(Cursor cursor) {
            int col_title = cursor.getColumnIndex(TITLE);
            int col_date = cursor.getColumnIndex(DATE_CREATED);
            int col_description = cursor.getColumnIndex(DESCRIPTION);

            String title = cursor.getString(col_title);
            Date date = DATE_FORMAT.parse(cursor.getString(col_date), new ParsePosition(0));

            Process p = new Process(title, date);
            p.setDescription(cursor.getString(col_description));

            return p;
        }
    };

    public Processes(SQLHelper sql) {
        super(sql);
    }

    public void create(String title) {
        SQLiteDatabase db = this.sql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_CREATED, System.currentTimeMillis());
        values.put(TITLE, title);
        db.insert(TABLE, null, values);
    }

    public ModelCursor<Process> findAll() {
        SQLiteDatabase db = sql.getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null,
                null, null);

        //startManagingCursor(cursor);
        return new ModelCursor<>(cursor, parser);
    }
}
