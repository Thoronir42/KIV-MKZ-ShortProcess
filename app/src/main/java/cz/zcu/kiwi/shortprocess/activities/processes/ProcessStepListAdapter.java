package cz.zcu.kiwi.shortprocess.activities.processes;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.Interval;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessStep;
import cz.zcu.kiwi.widgetWrappers.EntityClickListener;
import cz.zcu.kiwi.widgetWrappers.ModelListAdapter;


public class ProcessStepListAdapter extends ModelListAdapter<ProcessStep> {

    private EntityClickListener<ProcessStep> onDelete;

    public ProcessStepListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public void setOnDelete(EntityClickListener<ProcessStep> onDelete) {
        this.onDelete = onDelete;
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
                onClick.onClick(ps);
            }
        });

        textCaption.setText(ps.getCaption());
        textDelay.setText(new Interval(ps.getIntervalAfterStart()).toString());

        if (onDelete != null) {
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onDelete != null) {
                        onDelete.onClick(ps);
                    }
                }
            });
            buttonDelete.setVisibility(View.VISIBLE);
        } else {
            buttonDelete.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }
}
