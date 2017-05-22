package cz.zcu.kiwi.shortprocess;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.Random;

import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
import cz.zcu.kiwi.shortprocess.model.service.Processes;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;

public class ProcessListActivity extends Activity {

    SQLHelper sql;

    private ListView processList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_list);

        this.sql = new SQLHelper(this);
        Processes processes = this.sql.getProcesses();

        processes.create("Process #" + (new Random()).nextInt(100));

        processList = (ListView) findViewById(R.id.process_list);
        ProcessListAdapter adapter = new ProcessListAdapter(this, R.layout.process_list_item);
        processList.setAdapter(adapter);

        ModelCursor<Process> cursor = processes.findAll();
        Log.v("ProcessListActivity", "Displaying " + cursor.getCount() + " processes");

        while (cursor.moveToNext()) {
            adapter.add(cursor.formatCurrent());
        }
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sql.close();
    }
}
