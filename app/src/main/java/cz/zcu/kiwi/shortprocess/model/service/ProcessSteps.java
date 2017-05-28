package cz.zcu.kiwi.shortprocess.model.service;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

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

    public static final int INTERVAL_SECOND = 0;
    public static final int INTERVAL_MINUTE = 1;
    public static final int INTERVAL_HOUR = 2;
    public static final int INTERVAL_DAY = 3;

    private ProcessStepParser parser;

    public ProcessSteps(SQLHelper sql) {
        super(sql);
    }

    public ModelCursor<ProcessStep> findStepsOfProcess(int process_id) {
        SQLiteDatabase db = sql.getReadableDatabase();
        String where = PROCESS_ID + " = ?";
        String[] whereValues = new String[]{"" + process_id};
        Cursor cursor = db.query(getTable(), null, where, whereValues, null,
                null, INTERVAL_AFTER_START + " ASC");

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

    /**
     * Normalizes provided interval units into second
     * Usage: <code>joinInterval(int seconds [, int minutes [, int hours [, int days]]]</code>
     * @param parts
     * @return interval in seconds
     */
    public static long joinInterval(@NonNull int... parts) {
        if(parts.length < 1 || parts.length > 4) {
            throw new IllegalArgumentException("Method takes 1 to 4 arguments, " + parts.length + " given");
        }
        long interval = 0; // interval [days]
        if(parts.length > INTERVAL_DAY) {
            interval += parts[INTERVAL_DAY];
        }
        interval *= 24;    // interval [hours]

        if(parts.length > INTERVAL_HOUR) {
            interval += parts[INTERVAL_HOUR];
        }
        interval *= 60;    // interval [minutes]

        if(parts.length > INTERVAL_MINUTE) {
            interval += parts[INTERVAL_MINUTE];
        }
        interval *= 60;    // interval [seconds]

        interval += parts[INTERVAL_SECOND];

        return interval;
    }

    public static int[] splitInterval(long intervalSinceStart) {
        long seconds = intervalSinceStart % 60;
        long minutes = intervalSinceStart / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        return new int[]{
                (int) seconds,
                (int) (minutes % 60),
                (int) (hours % 24),
                (int) days
        };
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
