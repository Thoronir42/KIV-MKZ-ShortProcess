package cz.zcu.kiwi.shortprocess.activities.processes;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
import cz.zcu.kiwi.shortprocess.model.service.Processes;
import cz.zcu.kiwi.widgetWrappers.EntityClickListener;
import cz.zcu.kiwi.widgetWrappers.ModelListAdapter;


public class ProcessListAdapter extends ModelListAdapter<Process> {

    private final DateFormat dateFormat;

    public EntityClickListener<Process> onStart;

    public ProcessListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
    }

    public void setOnStart(EntityClickListener<Process> onStart) {
        this.onStart = onStart;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View rowView = convertView != null ? convertView : inflater.inflate(R.layout.process_list_item, parent, false);

        TextView text_title = (TextView) rowView.findViewById(R.id.title);
        TextView text_createdOn = (TextView) rowView.findViewById(R.id.createdOn);
        TextView text_stepCount = (TextView) rowView.findViewById(R.id.processSteps);
        ImageView image_icon = (ImageView) rowView.findViewById(R.id.icon);
        Button buttonStart = (Button) rowView.findViewById(R.id.buttonStart);

        final Process p = getItem(position);
        ContentValues extras = p.extras();

        Date date = p.getDateCreated();
        String createdOn = getContext().getResources().getString(R.string.created_on) + ": ";
        createdOn += date != null ? dateFormat.format(date) : "";

        String stepCount = getContext().getResources().getString(R.string.process_steps) + ": ";
        stepCount += extras.getAsInteger(Processes.EXTRA_STEP_COUNT);

        text_title.setText(p.getTitle());
        text_createdOn.setText(createdOn);
        text_stepCount.setText(stepCount);
        image_icon.setImageResource(R.mipmap.ic_launcher);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(p);
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStart.onClick(p);
            }
        });

        return rowView;
    }
}
