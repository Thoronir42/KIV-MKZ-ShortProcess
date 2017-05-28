package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Arrays;
import java.util.Date;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessStep;

public class Processes extends BaseModelHelper<Process> {
    public static final String TABLE = "sp__process";

    public static final String DATE_CREATED = "date_created";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";

    public static final String EXTRA_STEP_COUNT = "step_count";

    public Processes(SQLiteHelper sql) {
        super(sql);
    }


    public ModelCursor<Process> findProcessesWithStepCounts() {
        String select = "SELECT p.*" +
                ", COUNT(ps._id) AS " + EXTRA_STEP_COUNT +
                " FROM " + TABLE + " p" +
                " LEFT JOIN " + ProcessSteps.TABLE + " ps ON p._id = ps.process_id" +
                " GROUP BY p._id";

        SQLiteDatabase db = sql.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        ProcessParser parser = new ProcessParser() {
            @Override
            public Process parse(Cursor c) {
                Process process = super.parse(c);
                Log.d("Processes", "Column has these columns: " + Arrays.toString(c.getColumnNames()));
                int stepCount = c.getInt(c.getColumnIndex(EXTRA_STEP_COUNT));

                Log.d("Processes", "Process " + process.getTitle() + " has " + stepCount + " steps");

                ContentValues extras = process.extras();
                extras.put(EXTRA_STEP_COUNT, stepCount);

                return process;
            }
        };

        //startManagingCursor(cursor);
        return new ModelCursor<>(cursor, parser);
    }

    @Override
    public int delete(@NonNull Process entity) {
        int deleted = super.delete(entity);
        if(deleted > 0) {
            deleted += sql.getProcessSteps().delete(ProcessSteps.PROCESS_ID + " = ?",
                    new String[]{"" + entity.getId()}, ProcessStep.class.getSimpleName());
        }
        return deleted;
    }

    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected ProcessParser getEntityParser() {
        return new ProcessParser();
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

            values.put(DATE_CREATED, p.getDateCreated().getTime());
            values.put(TITLE, p.getTitle());
            values.put(DESCRIPTION, p.getDescription());
            values.put(DATE_CREATED, p.getDateCreated().getTime());

            return values;
        }
    }


}
