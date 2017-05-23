package cz.zcu.kiwi.shortprocess.activities.processes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import java.text.DateFormat;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
import cz.zcu.kiwi.shortprocess.model.service.Processes;


public class EditProcessActivity extends AppCompatActivity {

    private SQLHelper db;
    private DateFormat dateFormat;

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

        this.textTitle = (EditText)findViewById(R.id.title);
        this.textDescription = (EditText)findViewById(R.id.description);
        this.listProcessSteps = (ListView)findViewById(R.id.process_steps);

        this.processSteps = new ProcessStepListAdapter(this, R.layout.process_step_list_item);
        this.listProcessSteps.setAdapter(this.processSteps);

        int id = getIntent().getIntExtra(Processes.ID, -1);
        Log.v("EditProcessActivity", "Editing process with id " + id);

        this.setProcess(id);
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
        if(process == null) {
            Log.w("EditProcessActivity", "Attempted to setProcess() with null value");
            return;
        }

        Log.i("EditProcessActivity", "Process date is: " + process.getDate_created().toString());
        this.textTitle.setText(process.getTitle());
        this.textDescription.setText(process.getDescription());
        this.processSteps.setItems(this.db.getProcessSteps().findStepsOfProcess(process.getId()));
    }
}
