package cz.zcu.kiwi.shortprocess.activities.processes;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.Interval;
import cz.zcu.kiwi.shortprocess.model.service.ProcessSteps;

public class ProcessStepDialog extends DialogFragment {

    Bundle stepArgs = new Bundle();

    private EditText textCaption;
    private EditText textDescription;

    private NumberPicker numberDays;
    private NumberPicker numberHours;
    private NumberPicker numberMinutes;
    private NumberPicker numberSeconds;
    private DialogInterface.OnClickListener onSave;

    public void setArguments(Bundle bundle) {
        stepArgs = bundle;
//        stepArgs.getLong(ProcessSteps.ID, -1);
    }

    public void setOnSave(DialogInterface.OnClickListener onSave) {
        this.onSave = onSave;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.process_step_dialog, null);

        textCaption = (EditText) view.findViewById(R.id.textCaption);
        textDescription = (EditText) view.findViewById(R.id.textDescription);

        numberDays = (NumberPicker) view.findViewById(R.id.numberDays);
        numberHours = (NumberPicker) view.findViewById(R.id.numberHours);
        numberMinutes = (NumberPicker) view.findViewById(R.id.numberMinutes);
        numberSeconds = (NumberPicker) view.findViewById(R.id.numberSeconds);

        textCaption.setText(stepArgs.getString(ProcessSteps.CAPTION, ""));
        textDescription.setText(stepArgs.getString(ProcessSteps.DESCRIPTION, ""));

        Interval i = new Interval(stepArgs.getLong(ProcessSteps.INTERVAL_AFTER_START, 0));

        numberDays.setValue(i.getDays());
        numberHours.setValue(i.getHours());
        numberMinutes.setValue(i.getMinutes());
        numberSeconds.setValue(i.getSeconds());

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.save, this.onSave)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ProcessStepDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public Bundle getStepArgs() {
        Bundle b = new Bundle();
        Interval interval = new Interval(numberSeconds.getValue(), numberMinutes.getValue(),
                numberHours.getValue(), numberDays.getValue());

        b.putString(ProcessSteps.CAPTION, textCaption.getText().toString());
        b.putString(ProcessSteps.DESCRIPTION, textDescription.getText().toString());

        b.putLong(ProcessSteps.INTERVAL_AFTER_START, interval.toSeconds());

        return b;
    }
}
