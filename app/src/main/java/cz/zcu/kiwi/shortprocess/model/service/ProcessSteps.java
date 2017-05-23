package cz.zcu.kiwi.shortprocess.model.service;

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
    }
}
