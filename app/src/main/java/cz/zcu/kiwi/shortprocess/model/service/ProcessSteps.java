package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessStep;

public class ProcessSteps extends BaseModelHelper {
    public static final String TABLE = "sp__process_step";

    public static final String PROCESS_ID = "process_id";
    public static final String INTERVAL_AFTER_START = "interval_after_start";
    public static final String CAPTION = "caption";
    public static final String DESCRIPTION = "description";

    private ProcessStepParser parser;

    public ProcessSteps(SQLHelper sql) {
        super(sql);
    }

    public ModelCursor<ProcessStep> findStepsOfProcess(int process_id) {
        SQLiteDatabase db = sql.getReadableDatabase();
        String where = PROCESS_ID + " = ?";
        String[] whereValues = new String[]{"" + process_id};
        Cursor cursor = db.query(getTable(), null, where, whereValues, null,
                null, null);

        return new ModelCursor<>(cursor, getEntityParser());
    }

    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected ProcessStepParser getEntityParser() {
        if (parser == null) {
            parser = new ProcessStepParser();
        }

        return parser;
    }

    private static class ProcessStepParser extends EntityParser<ProcessStep> {

        @Override
        public ProcessStep parse(Cursor c) {
            int processId = c.getInt(c.getColumnIndex(PROCESS_ID));

            ProcessStep ps = new ProcessStep(processId);

            ps.setId(c.getInt(c.getColumnIndex(ID)));
            ps.setIntervalAfterStart(c.getInt(c.getColumnIndex(INTERVAL_AFTER_START)));
            ps.setCaption(c.getString(c.getColumnIndex(CAPTION)));
            ps.setDescription(c.getString(c.getColumnIndex(DESCRIPTION)));

            return ps;
        }

        @Override
        public ContentValues parse(ProcessStep entity) {
            ContentValues values = new ContentValues();

            values.put(PROCESS_ID, entity.getProcessId());
            values.put(INTERVAL_AFTER_START, entity.getIntervalAfterStart());
            values.put(CAPTION, entity.getCaption());
            values.put(DESCRIPTION, entity.getDescription());

            return values;
        }
    }
}
