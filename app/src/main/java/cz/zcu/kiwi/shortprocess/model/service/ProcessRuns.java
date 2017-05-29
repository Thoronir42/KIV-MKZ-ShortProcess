package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.database.sqlite.SQLiteQueryBuilder;

import java.util.Date;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRun;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessStep;

public final class ProcessRuns extends BaseModelHelper<ProcessRun> {
    public static final String TABLE = "sp__execution";

    public static final String ID_PROCESS = "id_process";
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


        String select = "SELECT pr.*" +
                ", p." + Processes.TITLE + " AS " + EXTRA_PROCESS_TITLE +
                ", COUNT(ps._id) AS " + EXTRA_TOTAL_STEPS +
                ", COUNT(CASE prs._id IS NOT NULL THEN 1 END) as " + EXTRA_COMPLETED_STEPS +
                " FROM " + TABLE + " pr" +
                " JOIN " + Processes.TABLE + " p ON pr." + ID_PROCESS + " = p" + Processes.ID +
                " LEFT JOIN " + ProcessSteps.TABLE + " ps ON ps." + ProcessSteps.PROCESS_ID + " = p" + Processes.ID +
                " LEFT JOIN " + ProcessRunSteps.TABLE + "prs ON prs." + ProcessRunSteps.PROCESS_RUN_ID + " = pr." + ProcessRuns.ID +
                "    AND prs." + ProcessRunSteps.PROCESS_STEP_ID + " = ps." + ProcessSteps.ID +
                " WHERE pr." + DATE_FINISHED + " IS NOT NULL";

        Cursor c = db.rawQuery(select, null);

        ProcessRunParser parser = new ProcessRunParser() {
            @Override
            public ProcessRun parse(Cursor c) {
                ProcessRun run = super.parse(c);
                ContentValues extras = run.extras();

                extras.put(EXTRA_PROCESS_TITLE, c.getString(c.getColumnIndex(Processes.TITLE)));
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
            long processId = c.getLong(c.getColumnIndex(ID_PROCESS));
            Date dateStarted = new Date(c.getLong(c.getColumnIndex(DATE_STARTED)));
            Date dateFinished = new Date(c.getLong(c.getColumnIndex(DATE_FINISHED)));

            ProcessRun run = new ProcessRun(processId, dateStarted);
            run.setDateFinished(dateFinished);

            return run;
        }

        @Override
        public ContentValues parse(ProcessRun entity) {
            ContentValues cv = new ContentValues();

            cv.put(ID_PROCESS, entity.getProcessId());
            cv.put(DATE_STARTED, entity.getDateStarted().getTime());
            cv.put(DATE_FINISHED, entity.getDateFinished().getTime());

            return cv;
        }
    }
}
