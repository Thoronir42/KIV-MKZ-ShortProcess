package cz.zcu.kiwi.shortprocess.activities.runs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessRunStep;
import cz.zcu.kiwi.shortprocess.model.service.ProcessRunSteps;

public class ProcessRunStepsDialog extends DialogFragment {

    private Collection<ProcessRunStep> items;

    private ProcessRunStepsAdapter listAdapter;
    private OnSave onSave;

    public void setItems(Collection<ProcessRunStep> items) {
        this.items = items;
    }

    public void setOnSave(OnSave onSave) {
        this.onSave = onSave;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.process_run_steps_completed);

        listAdapter = new ProcessRunStepsAdapter(getActivity());
        listAdapter.setItems(items);

        final ListView view = new ListView(getActivity());
        view.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        view.setAdapter(listAdapter);


        for (int i = 0; i < view.getCount(); i++) {
            ProcessRunStep prs = (ProcessRunStep) view.getItemAtPosition(i);
            view.setItemChecked(i, prs.extras().getAsBoolean(ProcessRunSteps.EXTRA_IS_COMPLETED));
        }

        builder.setView(view);

        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<ProcessRunStep> inserts = new ArrayList<ProcessRunStep>();
                ArrayList<ProcessRunStep> deletes = new ArrayList<ProcessRunStep>();

                for(int i = 0; i < view.getCount(); i++) {
                    ProcessRunStep step = listAdapter.getItem(i);
                    boolean isCompleted = step.extras().getAsBoolean(ProcessRunSteps.EXTRA_IS_COMPLETED);
                    if(isCompleted && !view.isItemChecked(i)) {
                        deletes.add(step);
                    } else if (!isCompleted && view.isItemChecked(i)) {
                        inserts.add(step);
                    }
                }

                onSave.save(inserts, deletes);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getDialog().cancel();
            }
        });

        return builder.create();
    }

    private static class ProcessRunStepsAdapter extends ArrayAdapter<ProcessRunStep> {

        ProcessRunStepsAdapter(@NonNull Context context) {
            super(context, R.layout.simple_check_list_item);
        }

        void setItems(Collection<ProcessRunStep> items) {
            this.clear();

            this.addAll(items);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View rowView = convertView;

            if(rowView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.simple_check_list_item, parent, false);
            }

            final ListView listView = (ListView) parent;
            ProcessRunStep prs = getItem(position);


            CheckedTextView text = (CheckedTextView) rowView.findViewById(R.id.item);
            text.setText(prs.extras().getAsString(ProcessRunSteps.EXTRA_STEP_CAPTION));
            text.setChecked(listView.isItemChecked(position));

            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listView.setItemChecked(position, !listView.isItemChecked(position));
                }
            });

            return rowView;
        }
    }

    public interface OnSave {
        void save(Collection<ProcessRunStep> inserts, Collection<ProcessRunStep> deletes);
    }

}
