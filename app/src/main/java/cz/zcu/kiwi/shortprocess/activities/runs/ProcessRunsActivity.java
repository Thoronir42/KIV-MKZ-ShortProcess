package cz.zcu.kiwi.shortprocess.activities.runs;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLiteHelper;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRun;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRunStep;
import cz.zcu.kiwi.shortprocess.model.service.ProcessRunSteps;
import cz.zcu.kiwi.widgetWrappers.EntityClickListener;

public class ProcessRunsActivity extends AppCompatActivity {

    private SQLiteHelper db;

    private ProcessRunListAdapter processRuns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_runs);

        db = new SQLiteHelper(this);

        processRuns = new ProcessRunListAdapter(this, R.layout.process_run_list_item);
        processRuns.setOnStop(new EntityClickListener<ProcessRun>() {
            @Override
            public void onClick(ProcessRun entity) {
                if(db.getProcessRuns().delete(entity) > 0) {
                    Toast.makeText(ProcessRunsActivity.this, R.string.process_run_stopped, Toast.LENGTH_SHORT)
                            .show();
                    loadRuns();
                } else {
                    Toast.makeText(ProcessRunsActivity.this, R.string.delete_failed, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        processRuns.setOnClick(new EntityClickListener<ProcessRun>() {
            @Override
            public void onClick(ProcessRun entity) {
                showRunStepsDialog(entity);
            }
        });

        ListView listView_processRuns = (ListView) findViewById(R.id.activityProcessRuns_runsList);
        listView_processRuns.setAdapter(processRuns);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadRuns();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        db.close();
    }

    private void showRunStepsDialog(final ProcessRun processRun) {
        ProcessRunStepsDialog dialog = new ProcessRunStepsDialog();

        ArrayList<ProcessRunStep> items = new ArrayList<>();
        ModelCursor<ProcessRunStep> cursor = db.getProcessRunSteps().findStepsOfRun(processRun.getId());
        String msg = "Showing processRunstStepsDialog with processRun id = " + processRun.getId()
                + ". Step count = " + cursor.getCount();
        Log.i("ProcessRunsActivity", msg);
        while(cursor.moveToNext()){
            items.add(cursor.formatCurrent());
        }
        cursor.close();

        dialog.setItems(items);
        dialog.setOnSave(new ProcessRunStepsDialog.OnSave() {
            @Override
            public void save(Collection<ProcessRunStep> inserts, Collection<ProcessRunStep> deletes) {
                ProcessRunSteps processRunSteps = db.getProcessRunSteps();
                for(ProcessRunStep step : inserts) {
                    step = new ProcessRunStep(processRun.getId(), step.extras().getAsLong(ProcessRunSteps.EXTRA_STEP_ID));
                    String msg = String.format(Locale.getDefault(), "RunStep %d of step %d, run %d",
                            step.getId(), step.getProcessStepId(), step.getProcessRunId());
                    Log.i("ProcessRunsActivity", "Insert " + msg);
                    processRunSteps.insert(step);
                }
                for(ProcessRunStep step : deletes) { // todo: optimize
                    processRunSteps.delete(step);
                }

                loadRuns();
            }
        });

        dialog.show(getSupportFragmentManager(), "ProcessRunsActivity");
    }

    private void loadRuns() {
        ModelCursor<ProcessRun> cursor = db.getProcessRuns().findRunning();
        processRuns.setItems(cursor);
        cursor.close();
    }
}
