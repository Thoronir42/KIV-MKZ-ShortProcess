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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.entity.Process;
import cz.zcu.kiwi.shortprocess.model.entity.ProcessStep;


public class ProcessStepListAdapter extends ArrayAdapter<ProcessStep> {
    public ProcessStepListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView != null ? convertView : inflater.inflate(R.layout.process_list_item, parent, false);

        TextView text_title = (TextView) rowView.findViewById(R.id.title);
        ImageView image_icon = (ImageView) rowView.findViewById(R.id.icon);

        ProcessStep ps = getItem(position);

        text_title.setText(ps.getCaption());

        image_icon.setImageResource(R.mipmap.ic_launcher);

        return rowView;
    }

    public void setItems(ModelCursor<ProcessStep> items) {
        this.clear();

        Log.v("ProcessListAdapter", "Displaying " + items.getCount() + " processes");

        while (items.moveToNext()) {
            this.add(items.formatCurrent());
        }
    }
}
