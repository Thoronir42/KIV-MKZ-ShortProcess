package cz.zcu.kiwi.shortprocess.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Scanner;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.service.ProcessSteps;
import cz.zcu.kiwi.shortprocess.model.service.Processes;

/**
 * Helper to the database, manages versions and creation
 */
public class SQLHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "kiwi_short_process.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    private Processes processes;
    private ProcessSteps processSteps;

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("SQLHelper", "Creating DB");

        Scanner s = new Scanner(context.getResources().openRawResource(R.raw.short_process_create))
                .useDelimiter(";");
        while(s.hasNext()) {
            String query = s.next().trim();
            if(query.length() > 0) {
                Log.i("SQLHelper", "Query: " + query);
                db.execSQL(query);
            }

        }
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
