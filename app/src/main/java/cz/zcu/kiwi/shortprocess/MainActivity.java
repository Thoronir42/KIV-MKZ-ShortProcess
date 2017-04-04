package cz.zcu.kiwi.shortprocess;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

public class MainActivity extends Activity {
    EventDataSQLHelper eventsData;
    TextView output;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = (TextView) findViewById(R.id.output);

        eventsData = new EventDataSQLHelper(this);
        addEvent("Hello Android Event");
        Cursor cursor = getEvents();
        showEvents(cursor);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventsData.close();
    }

    private void addEvent(String title) {
        SQLiteDatabase db = eventsData.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventDataSQLHelper.TIME, System.currentTimeMillis());
        values.put(EventDataSQLHelper.TITLE, title);
        db.insert(EventDataSQLHelper.TABLE, null, values);

    }

    private Cursor getEvents() {
        SQLiteDatabase db = eventsData.getReadableDatabase();
        Cursor cursor = db.query(EventDataSQLHelper.TABLE, null, null, null, null,
                null, null);

        //startManagingCursor(cursor);
        return cursor;
    }

    private void showEvents(Cursor cursor) {
        StringBuilder ret = new StringBuilder("Saved Events:\n\n");
        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            long time = cursor.getLong(1);
            String title = cursor.getString(2);
            String datum = (String) DateFormat.format("dd/MM/yyyy hh:mm:ss", time);
            // ret.append(id + ": " + time + ": " + title + "\n");
            ret.append(id + ": " + datum + ": " + title + "\n");
        }
        output.setText(ret);
    }
}