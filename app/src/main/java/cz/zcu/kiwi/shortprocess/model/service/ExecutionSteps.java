package cz.zcu.kiwi.shortprocess.model.service;

import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;

public class ExecutionSteps extends BaseModelHelper {
    public static final String TABLE = "sp__execution_step";

    public static final String EXECUTION_ID = "execution_id";
    public static final String PROCESS_STEP_ID = "process_step_id";
    public static final String NOTIFIED_ON = "notified_on";
    public static final String STATUS = "status";

    public static final int STATUS_SCHEDULED = 0;
    public static final int STATUS_PROCESSED = 1;

    ExecutionSteps(SQLHelper sql) {
        super(sql);
    }

    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected ModelCursor.Parser getEntityParser() {
        throw new UnsupportedOperationException();
    }
}
