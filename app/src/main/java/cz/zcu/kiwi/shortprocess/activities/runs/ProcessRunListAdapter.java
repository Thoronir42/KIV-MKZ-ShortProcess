package cz.zcu.kiwi.shortprocess.activities.runs;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRun;
import cz.zcu.kiwi.shortprocess.model.service.ProcessRuns;
import cz.zcu.kiwi.widgetWrappers.EntityClickListener;
import cz.zcu.kiwi.widgetWrappers.ModelListAdapter;

public class ProcessRunListAdapter extends ModelListAdapter<ProcessRun> {

    private final Resources resources;
    private DateFormat dateFormat;
    private EntityClickListener<ProcessRun> onStop;

    public ProcessRunListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);

        dateFormat = android.text.format.DateFormat.getDateFormat(context);
        resources = getContext().getResources();
    }

    public void setOnStop(EntityClickListener<ProcessRun> onStop) {
        this.onStop = onStop;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView != null ? convertView : inflater.inflate(R.layout.process_list_item, parent, false);

        TextView text_title = (TextView) rowView.findViewById(R.id.processRunListItem_textTitle);
        TextView text_startedOn = (TextView) rowView.findViewById(R.id.processRunListItem_textStartedOn);
        TextView text_runCompletion = (TextView) rowView.findViewById(R.id.processRunListItem_completion);

        Button buttonStop = (Button) rowView.findViewById(R.id.processRunListItem_buttonStop);

        final ProcessRun pr = getItem(position);
        if(pr == null) {
            return rowView;
        }
        ContentValues extras = pr.extras();

        String title = String.format(Locale.getDefault(),
                "[%d] %s", pr.getId(), extras.getAsString(ProcessRuns.EXTRA_PROCESS_TITLE));

        Date date = pr.getDateStarted();

        String startedOn = resources.getString(R.string.started_on) + ": ";
        startedOn += date != null ? dateFormat.format(date) : "";

        String stepCount = String.format(Locale.getDefault(), "%s: %d / %d",
                resources.getString(R.string.process_steps),
                extras.getAsInteger(ProcessRuns.EXTRA_COMPLETED_STEPS),
                extras.getAsInteger(ProcessRuns.EXTRA_TOTAL_STEPS));


        text_title.setText(title);
        text_startedOn.setText(startedOn);
        text_runCompletion.setText(stepCount);

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStop.onClick(pr);
            }
        });

        return rowView;
    }
}
