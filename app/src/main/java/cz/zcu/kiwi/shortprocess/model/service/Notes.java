package cz.zcu.kiwi.shortprocess.model.service;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;

public class Notes extends BaseModelHelper {
    public static final String TABLE = "sp__note";

    public static final String PROCESS_ID = "process_id";
    public static final String PROCESS_STEP_ID = "process_step_id";
    public static final String EXECUTION_ID = "execution_id";
    public static final String ATTACHED_FILE = "attached_file";
    public static final String CONTENT = "content";

    Notes(SQLiteHelper sql) {
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
