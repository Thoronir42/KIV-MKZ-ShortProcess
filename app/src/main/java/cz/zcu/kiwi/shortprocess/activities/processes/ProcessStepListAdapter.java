package cz.zcu.kiwi.shortprocess.activities.processes;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.Interval;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessStep;


public class ProcessStepListAdapter extends ArrayAdapter<ProcessStep> {

    private ProcessStepClickListener onDelete;
    private ProcessStepClickListener onClick;

    public ProcessStepListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public void setOnDelete(ProcessStepClickListener onDelete) {
        this.onDelete = onDelete;
    }

    public void setOnClick(ProcessStepClickListener onClick) {
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView != null ? convertView : inflater.inflate(R.layout.process_step_list_item, parent, false);

        TextView textCaption = (TextView) rowView.findViewById(R.id.stepListItem_caption);
        TextView textDelay = (TextView) rowView.findViewById(R.id.stepListItem_delay);
        Button buttonDelete = (Button) rowView.findViewById(R.id.stepListItem_buttonDelete);

        final ProcessStep ps = getItem(position);
        if (ps == null) {
            Log.e("ProcessStepListAdapter", "Process step not found");
            return rowView;
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(ps.getId());
            }
        });

        textCaption.setText(ps.getCaption());
        textDelay.setText(new Interval(ps.getIntervalAfterStart()).toString());

        if (onDelete != null) {
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onDelete.onClick(ps.getId());
                }
            });
            buttonDelete.setVisibility(View.VISIBLE);
        } else {
            buttonDelete.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }

    public void setItems(ModelCursor<ProcessStep> items) {
        this.clear();

        Log.v("ProcessListAdapter", "Displaying " + items.getCount() + " processes");

        while (items.moveToNext()) {
            this.add(items.formatCurrent());
        }
    }

    public static abstract class ProcessStepClickListener {
        void onClick(long processStepId) {

        }

        void onLongClick(long processStepId) {

        }
    }
}
