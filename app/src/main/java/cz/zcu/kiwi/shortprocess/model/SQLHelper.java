package cz.zcu.kiwi.shortprocess.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cz.zcu.kiwi.shortprocess.model.service.ProcessSteps;
import cz.zcu.kiwi.shortprocess.model.service.Processes;

/**
 * Helper to the database, manages versions and creation
 */
public class SQLHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kiwi_short_process.db";
    private static final int DATABASE_VERSION = 1;

    private Processes processes;
    private ProcessSteps processSteps;

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        throw new UnsupportedOperationException();
        /*
        String sql = "loaded file";
        Log.d("EventsData", "onCreate: " + sql);
        db.execSQL(sql);
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion)
            return;

        throw new UnsupportedOperationException();

//        String sql = null;
//        if (oldVersion == 1)
//            sql = "alter table " + TABLE + " add note text;";
//        if (oldVersion == 2)
//            sql = "";
//
//        Log.d("EventsData", "onUpgrade	: " + sql);
//        if (sql != null)
//            db.execSQL(sql);
    }

    public Processes getProcesses() {
        if(this.processes == null) {
            this.processes = new Processes(this);
            Log.v("SQLHelper", "Created Processes helper class");
        }

        return this.processes;
    }

    public ProcessSteps getProcessSteps() {
        if(this.processSteps == null) {
            this.processSteps = new ProcessSteps(this);
            Log.v("SQLHelper", "Created Processes helper class");
        }

        return this.processSteps;
    }



}
