package cz.zcu.kiwi.shortprocess;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import java.util.Random;

import cz.zcu.kiwi.shortprocess.model.service.Processes;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;

public class MainActivity extends Activity {
    SQLHelper sql;
    TextView output;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

        this.sql = new SQLHelper(this);
        Processes processes = this.sql.getProcesses();

        processes.create("Process #" + (new Random()).nextInt(100));



        Cursor cursor = processes.findAll();
        showEvents(cursor);
        cursor.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sql.close();
    }

    private void showEvents(Cursor cursor) {
        StringBuilder ret = new StringBuilder("Saved Events:\n\n");
        cursor.getCount();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            long time = cursor.getLong(1);
            String title = cursor.getString(2);
            String datum = DateFormat.format("dd/MM/yyyy hh:mm:ss", time).toString();
            // ret.append(id + ": " + time + ": " + title + "\n");
            ret.append(id).append(": ").append(datum).append(": ").append(title).append("\n");
        }
        output.setText(ret);
    }
}