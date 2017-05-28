package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRun;

public class ProcessRuns extends BaseModelHelper<ProcessRun> {
    public static final String TABLE = "sp__execution";

    public static final String ID_PROCESS = "id_process";
    public static final String DATE_STARTED = "date_started";
    public static final String DATE_FINISHED = "date_finished";

    public ProcessRuns(SQLiteHelper sql) {
        super(sql);
    }

    @Override
    protected String getTable() {
        return TABLE;
    }

    @Override
    protected ProcessRunParser getEntityParser() {
        throw new UnsupportedOperationException();
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
