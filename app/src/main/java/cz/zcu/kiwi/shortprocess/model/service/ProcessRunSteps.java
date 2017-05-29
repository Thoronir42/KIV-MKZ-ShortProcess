package cz.zcu.kiwi.shortprocess.model.service;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;

public class ProcessRunSteps extends BaseModelHelper {
    public static final String TABLE = "sp__execution_step";

    public static final String PROCESS_RUN_ID = "execution_id";
    public static final String PROCESS_STEP_ID = "process_step_id";
    public static final String NOTIFIED_ON = "notified_on";
    public static final String STATUS = "status";

    public static final int STATUS_SCHEDULED = 0;
    public static final int STATUS_PROCESSED = 1;

    ProcessRunSteps(SQLiteHelper sql) {
        super(sql);
    }

    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected EntityParser getEntityParser() {
        throw new UnsupportedOperationException();
    }
}
