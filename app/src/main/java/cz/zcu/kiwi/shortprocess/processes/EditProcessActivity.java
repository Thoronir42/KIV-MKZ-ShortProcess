package cz.zcu.kiwi.shortprocess.processes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import java.util.Date;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
import cz.zcu.kiwi.shortprocess.model.service.Processes;


public class EditProcessActivity extends AppCompatActivity {

    private SQLHelper db;

    private EditText textTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_process);

        this.db = new SQLHelper(this);

        this.textTitle = (EditText)findViewById(R.id.title);
        int id = getIntent().getIntExtra(Processes.ID, -1);
        Log.v("EditProcessActivity", "Editing process with id " + id);

        setProcess(this.db.getProcesses().find(id));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        this.db.close();
    }

    public void setProcess(Process process) {
        if(process == null) {
            Log.w("EditProcessActivity", "Attempted to setProcess() with null value");
            return;
        }
        this.textTitle.setText(process.getTitle());
    }
}
