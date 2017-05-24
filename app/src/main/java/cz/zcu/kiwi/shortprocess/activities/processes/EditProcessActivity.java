package cz.zcu.kiwi.shortprocess.activities.processes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
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

        this.init(getIntent());
    }

    private void init(Intent intent) {
        switch (intent.getAction()) {
            case ACTION_CREATE:
                this.process = null;
                break;
            case ACTION_EDIT:
                int id = intent.getIntExtra(Processes.ID, -1);
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

    public void setProcess(int process_id) {
        setProcess(this.db.getProcesses().find(process_id));
    }

    public void setProcess(Process process) {
        if (process == null) {
            Log.w("EditProcessActivity", "Attempted to setProcess() with null value");
            return;
        }

        Log.i("EditProcessActivity", "Process date is: " + process.getDate_created().toString());
        this.process = process;
        this.textTitle.setText(process.getTitle());
        this.textDescription.setText(process.getDescription());
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
            }

        } else {
            this.process.setTitle(title);
            this.process.setDescription(description);
            if (processes.update(this.process) > 0) {
                Toast.makeText(this, R.string.process_updated, Toast.LENGTH_LONG)
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
}
