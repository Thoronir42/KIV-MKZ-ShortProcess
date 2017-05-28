package cz.zcu.kiwi.shortprocess.activities.runs;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import cz.zcu.kiwi.shortprocess.model.entity.ProcessRun;
import cz.zcu.kiwi.widgetWrappers.ModelListAdapter;

public class ProcessRunListAdapter extends ModelListAdapter<ProcessRun> {
    public ProcessRunListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
}
