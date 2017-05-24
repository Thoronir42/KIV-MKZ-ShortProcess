package cz.zcu.kiwi.shortprocess.activities.processes;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Random;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
import cz.zcu.kiwi.shortprocess.model.service.Processes;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;

public class ProcessListActivity extends AppCompatActivity {

    SQLHelper sql;

    private ListView processList;
    private ProcessListAdapter processAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_list);

        this.sql = new SQLHelper(this);
        Processes processes = this.sql.getProcesses();

        processAdapter = new ProcessListAdapter(this, R.layout.process_list_item);
        processList = prepareProcessList(processAdapter);

        ModelCursor<Process> cursor = processes.findAll();
        processAdapter.setItems(cursor);
        cursor.close();


        /*toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.v("ProcessListActivity", "Toolbar item clicked of id " + item.getItemId());
                return false;
            }
        });*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Context context = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProcessActivity.class);
                intent.setAction(EditProcessActivity.ACTION_CREATE);
                startActivity(intent);
            }
        });
    }

    private ListView prepareProcessList(ProcessListAdapter adapter) {
        ListView view = (ListView) findViewById(R.id.process_list);
        view.setAdapter(adapter);

        final Context context = this;

        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Process process = (Process) parent.getItemAtPosition(position);

                Intent intent = new Intent(context, EditProcessActivity.class);
                intent.setAction(EditProcessActivity.ACTION_EDIT);

                intent.putExtra(Processes.ID, process.getId());

                Log.v("ProcessListActivity", "Starting editProcessActivity with id " + process.getId());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sql.close();
    }
}
