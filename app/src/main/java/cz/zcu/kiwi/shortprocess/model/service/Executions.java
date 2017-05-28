package cz.zcu.kiwi.shortprocess.model.service;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;
import cz.zcu.kiwi.shortprocess.model.entity.Execution;

public class Executions extends BaseModelHelper {
    public static final String TABLE = "sp__execution";

    public static final String ID_PROCESS = "id_process";
    public static final String DATE_STARTED = "date_started";
    public static final String DATE_FINISHED = "date_finished";

    Executions(SQLiteHelper sql) {
        super(sql);
    }

    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected EntityParser<Execution> getEntityParser() {
        throw new UnsupportedOperationException();
    }
}
