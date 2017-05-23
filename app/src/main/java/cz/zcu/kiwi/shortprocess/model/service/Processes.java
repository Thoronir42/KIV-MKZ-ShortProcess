package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParsePosition;
import java.util.Date;

import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;
import cz.zcu.kiwi.shortprocess.model.entity.Process;

public class Processes extends BaseModelHelper<Process> {
    public static final String TABLE = "sp__process";

    public static final String DATE_CREATED = "date_created";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";

    private ProcessParser parser;

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


    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected ProcessParser getEntityParser() {
        if (parser == null) {
            parser = new ProcessParser();
        }

        return parser;
    }

    private static class ProcessParser extends ModelCursor.Parser {

        @Override
        public Process parse(Cursor c) {
            String title = c.getString(c.getColumnIndex(TITLE));

            String dateString = c.getString(c.getColumnIndex(DATE_CREATED));
            Date date = DATE_FORMAT.parse(dateString, new ParsePosition(0));

            Process p = new Process(title, date);
            p.setId(c.getInt(c.getColumnIndex(ID)));
            p.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));

            return p;
        }
    }


}
