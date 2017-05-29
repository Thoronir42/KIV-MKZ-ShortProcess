package cz.zcu.kiwi.shortprocess.activities.runs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRun;

public class ProcessRunsActivity extends AppCompatActivity {

    private SQLiteHelper db;

    private ProcessRunListAdapter processRuns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_runs);

        db = new SQLiteHelper(this);

        processRuns = new ProcessRunListAdapter(this, R.layout.process_run_list_item);
        ListView listView_processRuns = (ListView) findViewById(R.id.activityProcessRuns_runsList);
        listView_processRuns.setAdapter(processRuns);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ModelCursor<ProcessRun> cursor = db.getProcessRuns().findRunning();
        processRuns.setItems(cursor);
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        db.close();
    }
}
