package cz.zcu.kiwi.shortprocess.model.service;

import cz.zcu.kiwi.shortprocess.model.SQLHelper;

public class Executions extends BaseModelHelper {
    public static final String TABLE = "sp__execution";

    public static final String ID_PROCESS = "id_process";
    public static final String DATE_STARTED = "date_started";
    public static final String DATE_FINISHED = "date_finished";

    Executions(SQLHelper sql) {
        super(sql);
    }
}
