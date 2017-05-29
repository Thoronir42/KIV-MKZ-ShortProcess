package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRun;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRunStep;

public class ProcessRunSteps extends BaseModelHelper<ProcessRunStep> {
    public static final String TABLE = "sp__execution_step";

    public static final String PROCESS_RUN_ID = "execution_id";
    public static final String PROCESS_STEP_ID = "process_step_id";
    public static final String NOTIFIED_ON = "notified_on";
    public static final String STATUS = "status";

    public static final int STATUS_SCHEDULED = 0;
    public static final int STATUS_PROCESSED = 1;

    public static final String EXTRA_STEP_CAPTION = "_step_caption";
    public static final String EXTRA_IS_COMPLETED = "_is_completed";

    public ProcessRunSteps(SQLiteHelper sql) {
        super(sql);
    }

    public ModelCursor<ProcessRunStep> findStepsOfRun(long processRunId) {
        String select = "SELECT prs.*" +
                ",   ps." + ProcessSteps.CAPTION + " AS " + EXTRA_STEP_CAPTION +
                ",   CASE WHEN prs._id IS NOT NULL THEN 1 ELSE 0 END AS " + EXTRA_IS_COMPLETED +
                " FROM " + TABLE + " prs" +
                " JOIN " + ProcessRuns.TABLE + " pr" +
                "   ON pr." + ProcessRuns.ID + " = ?" +
                " JOIN " + Processes.TABLE + " p" +
                "   ON p." + Processes.ID + " = pr." + ProcessRuns.PROCESS_ID +
                " JOIN " + ProcessSteps.TABLE + " ps" +
                "   ON ps." + ProcessSteps.PROCESS_ID + " = p." + Processes.ID;

        String[] where = {"" + processRunId};
        Cursor c = sql.getReadableDatabase().rawQuery(select, where);

        ProcessRunStepParser parser = new ProcessRunStepParser() {
            @Override
            public ProcessRunStep parse(Cursor c) {
                ProcessRunStep step = super.parse(c);
                ContentValues extras = step.extras();

                extras.put(EXTRA_STEP_CAPTION, c.getString(c.getColumnIndex(EXTRA_STEP_CAPTION)));
                extras.put(EXTRA_IS_COMPLETED, c.getInt(c.getColumnIndex(EXTRA_IS_COMPLETED)) != 0);
                return step;
            }
        };

        return new ModelCursor<>(c, parser);
    }

    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected EntityParser getEntityParser() {
        return new ProcessRunStepParser();
    }

    static class ProcessRunStepParser extends EntityParser<ProcessRunStep> {

        @Override
        public ProcessRunStep parse(Cursor c) {
            long processRunId = c.getLong(c.getColumnIndex(PROCESS_RUN_ID));
            long processStepId = c.getLong(c.getColumnIndex(PROCESS_STEP_ID));
            ProcessRunStep prs = new ProcessRunStep(processRunId, processStepId);
            prs.setId(c.getLong(c.getColumnIndex(ID)));

            long notifiedOn = c.getLong(c.getColumnIndex(NOTIFIED_ON));
            prs.setNotifiedOn(notifiedOn > 0 ? new Date(notifiedOn) : null);
            prs.setStatus(c.getInt(c.getColumnIndex(STATUS)));

            return prs;
        }

        @Override
        public ContentValues parse(ProcessRunStep entity) {
            ContentValues cv = new ContentValues();
            cv.put(PROCESS_RUN_ID, entity.getProcessRunId());
            cv.put(PROCESS_STEP_ID, entity.getProcessStepId());
            cv.put(STATUS, entity.getStatus());

            Date notifiedOn = entity.getNotifiedOn();
            cv.put(NOTIFIED_ON, notifiedOn == null ? null : notifiedOn.getTime());

            return cv;
        }
    }
}
