package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Date;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
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

    private static class ProcessParser extends EntityParser<Process> {

        @Override
        public Process parse(Cursor c) {
            String title = c.getString(c.getColumnIndex(TITLE));

            Process p = new Process(title, new Date(c.getLong(c.getColumnIndex(DATE_CREATED))));
            p.setId(c.getInt(c.getColumnIndex(ID)));
            p.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));

            return p;
        }

        public ContentValues parse(Process p) {
            ContentValues values = new ContentValues();

            // values.put(DATE_CREATED, p.getDate_created().getTime()); date created should not be updated
            values.put(TITLE, p.getTitle());
            values.put(DESCRIPTION, p.getDescription());

            return values;
        }
    }


}
