package cz.zcu.kiwi.shortprocess.model.service;

import cz.zcu.kiwi.shortprocess.model.SQLHelper;

public class ProcessSteps extends BaseModelHelper {
    public static final String TABLE = "sp__process_step";

    public static final String PROCESS_ID = "process_id";
    public static final String INTERVAL_AFTER_START = "interval_after_start";
    public static final String CAPTION = "caption";
    public static final String DESCRIPTION = "description";

    public ProcessSteps(SQLHelper sql) {
        super(sql);
    }
}
