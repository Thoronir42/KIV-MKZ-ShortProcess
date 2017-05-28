package cz.zcu.kiwi.shortprocess.activities.processes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessStep;
import cz.zcu.kiwi.shortprocess.model.service.ProcessSteps;
import cz.zcu.kiwi.shortprocess.model.service.Processes;


public class EditProcessActivity extends AppCompatActivity {

    public static final String ACTION_CREATE = "create";
    public static final String ACTION_EDIT = "edit";

    private SQLHelper db;
    private DateFormat dateFormat;

    private Process process;

    private EditText textTitle;
    private EditText textDescription;
    private ListView listProcessSteps;
    private Button buttonAddStep;

    private ProcessStepListAdapter processSteps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_process);

        this.db = new SQLHelper(this);
        this.dateFormat = android.text.format.DateFormat.getDateFormat(this);

        this.textTitle = (EditText) findViewById(R.id.title);
        this.textDescription = (EditText) findViewById(R.id.description);
        this.listProcessSteps = (ListView) findViewById(R.id.process_steps);

        this.processSteps = new ProcessStepListAdapter(this, R.layout.process_step_list_item);
        this.listProcessSteps.setAdapter(this.processSteps);

        buttonAddStep = (Button) findViewById(R.id.button_add_step);
        buttonAddStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewStepDialog();
            }
        });

        this.init(getIntent());
    }

    private void init(Intent intent) {
        switch (intent.getAction()) {
            case ACTION_CREATE:
                this.setProcess(null);
                break;
            case ACTION_EDIT:
                long id = intent.getLongExtra(Processes.ID, -1);
                Log.v("EditProcessActivity", "Editing process with id " + id);
                this.setProcess(id);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_process_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_process:
                return saveProcess();
            case R.id.delete_process:
                return deleteProcess();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.db.close();
    }

    public void setProcess(long process_id) {
        setProcess(this.db.getProcesses().find(process_id));
    }

    public void setProcess(Process process) {
        this.process = process;
        if (process != null) {
            this.textTitle.setText(process.getTitle());
            this.textDescription.setText(process.getDescription());
            this.loadSteps();

            this.buttonAddStep.setVisibility(View.VISIBLE);
        } else {
            this.buttonAddStep.setVisibility(View.INVISIBLE);
        }
    }

    private void loadSteps() {
        this.processSteps.setItems(this.db.getProcessSteps().findStepsOfProcess(process.getId()));
    }

    private boolean saveProcess() {
        Processes processes = this.db.getProcesses();

        String title = this.textTitle.getText().toString();
        String description = this.textDescription.getText().toString();

        if (this.process == null) {
            Process p = new Process(title, System.currentTimeMillis());
            p.setDescription(description);

            if (processes.insert(p) != -1) {
                Toast.makeText(this, R.string.process_created, Toast.LENGTH_LONG)
                        .show();
                setProcess(p);
            } else {
                Toast.makeText(this, R.string.save_error, Toast.LENGTH_LONG)
                        .show();
            }

        } else {
            this.process.setTitle(title);
            this.process.setDescription(description);
            if (processes.update(this.process) > 0) {
                Toast.makeText(this, R.string.process_updated, Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(this, R.string.save_error, Toast.LENGTH_LONG)
                        .show();
            }
        }

        return true;
    }

    private boolean deleteProcess() {
        if (this.process == null) {
            Toast.makeText(this, R.string.delete_failed, Toast.LENGTH_SHORT)
                    .show();
            return false;
        }

        if (this.db.getProcesses().delete(this.process) > 1) {
            Toast.makeText(this, R.string.process_deleted, Toast.LENGTH_LONG)
                    .show();

        }
        Intent intent = new Intent(this, ProcessListActivity.class);
        startActivity(intent);
        return true;
    }

    private void showNewStepDialog() {
        final ProcessStepDialog processStepDialog = new ProcessStepDialog();
        processStepDialog.setOnSave(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle b = processStepDialog.getStepArgs();

                ProcessStep step = new ProcessStep(EditProcessActivity.this.process.getId());
                step.setCaption(b.getString(ProcessSteps.CAPTION));
                step.setDescription(b.getString(ProcessSteps.DESCRIPTION));
                step.setIntervalAfterStart(b.getLong(ProcessSteps.INTERVAL_AFTER_START));

                if (db.getProcessSteps().insert(step) > 0) {
                    Toast.makeText(EditProcessActivity.this, R.string.process_step_created, Toast.LENGTH_LONG);
                }
                ;
                loadSteps();
            }
        });
        processStepDialog.show(this.getSupportFragmentManager(), "newStepDialog");
    }
}
