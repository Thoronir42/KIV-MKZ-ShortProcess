package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.Date;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRun;

public final class ProcessRuns extends BaseModelHelper<ProcessRun> {
    public static final String TABLE = "sp__execution";

    public static final String PROCESS_ID = "process_id";
    public static final String DATE_STARTED = "date_started";
    public static final String DATE_FINISHED = "date_finished";

    public static final String EXTRA_TOTAL_STEPS = "_total_steps";
    public static final String EXTRA_COMPLETED_STEPS = "_completed_steps";
    public static final String EXTRA_PROCESS_TITLE = "_process_title";

    public ProcessRuns(SQLiteHelper sql) {
        super(sql);
    }

    public ModelCursor<ProcessRun> findRunning() {
        SQLiteDatabase db = this.sql.getReadableDatabase();


        String select = "SELECT" +
                "   pr.*" +
                ",  p." + Processes.TITLE + " AS " + EXTRA_PROCESS_TITLE +
                ",  COUNT(ps._id) AS " + EXTRA_TOTAL_STEPS +
                ",  COUNT(CASE WHEN prs._id IS NOT NULL THEN 1 END) AS " + EXTRA_COMPLETED_STEPS +
                " FROM " + TABLE + " pr" +
                " LEFT JOIN " + Processes.TABLE + " p" +
                "   ON p." + Processes.ID + " = pr." + PROCESS_ID +
                " LEFT JOIN " + ProcessSteps.TABLE + " ps" +
                "   ON ps." + ProcessSteps.PROCESS_ID + " = p." + Processes.ID +
                " LEFT JOIN " + ProcessRunSteps.TABLE + " prs" +
                "   ON prs." + ProcessRunSteps.PROCESS_RUN_ID + " = pr." + ProcessRuns.ID +
                "   AND prs." + ProcessRunSteps.PROCESS_STEP_ID + " = ps." + ProcessSteps.ID +
                " WHERE" +
                "   pr." + ID + " IS NOT NULL" +
                " GROUP BY" +
                "   pr." + ID;

        Cursor c = db.rawQuery(select, null);

        ProcessRunParser parser = new ProcessRunParser() {
            @Override
            public ProcessRun parse(Cursor c) {
                ProcessRun run = super.parse(c);
                ContentValues extras = run.extras();

                String processTitle = c.getString(c.getColumnIndex(EXTRA_PROCESS_TITLE));
                Log.i("ProcessRuns", "Process title of process " + run.getProcessId() + " is " + processTitle);
                extras.put(EXTRA_PROCESS_TITLE, processTitle);
                extras.put(EXTRA_TOTAL_STEPS, c.getInt(c.getColumnIndex(EXTRA_TOTAL_STEPS)));
                extras.put(EXTRA_COMPLETED_STEPS, c.getInt(c.getColumnIndex(EXTRA_COMPLETED_STEPS)));

                return run;
            }
        };

        return new ModelCursor<>(c, parser);
    }

    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected ProcessRunParser getEntityParser() {
        return new ProcessRunParser();
    }

    private static class ProcessRunParser extends EntityParser<ProcessRun> {

        @Override
        public ProcessRun parse(Cursor c) {
            long processId = c.getLong(c.getColumnIndex(PROCESS_ID));
            Date dateStarted = new Date(c.getLong(c.getColumnIndex(DATE_STARTED)));
            Date dateFinished = new Date(c.getLong(c.getColumnIndex(DATE_FINISHED)));

            ProcessRun run = new ProcessRun(processId, dateStarted);
            run.setId(c.getLong(c.getColumnIndex(ID)));
            run.setDateFinished(dateFinished);

            return run;
        }

        @Override
        public ContentValues parse(ProcessRun entity) {
            ContentValues cv = new ContentValues();

            Date dateStarted = entity.getDateStarted();
            Date dateFinished = entity.getDateFinished();

            cv.put(PROCESS_ID, entity.getProcessId());
            cv.put(DATE_STARTED, dateStarted == null ? null : dateStarted.getTime());
            cv.put(DATE_FINISHED, dateFinished == null ? null : dateFinished.getTime());

            return cv;
        }
    }
}
