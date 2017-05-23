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

import java.text.DateFormat;
import java.util.Date;

import cz.zcu.kiwi.shortprocess.R;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.entity.Process;


public class ProcessListAdapter extends ArrayAdapter<Process> {

    private final LayoutInflater inflater;

    private final DateFormat dateFormat;

    public ProcessListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dateFormat = android.text.format.DateFormat.getDateFormat(getContext());

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = convertView != null ? convertView : inflater.inflate(R.layout.process_list_item, parent, false);

        TextView text_title = (TextView) rowView.findViewById(R.id.title);
        TextView text_subtitle = (TextView) rowView.findViewById(R.id.subtitle);
        ImageView image_icon = (ImageView) rowView.findViewById(R.id.icon);

        Process p = getItem(position);

        text_title.setText(p.getTitle());

        Date date = p.getDate_created();
        text_subtitle.setText(date != null ? dateFormat.format(date) : "");

        image_icon.setImageResource(R.mipmap.ic_launcher);

        return rowView;
    }

    public void setItems(ModelCursor<Process> items) {
        Log.v("ProcessListAdapter", "Displaying " + items.getCount() + " processes");

        while (items.moveToNext()) {
            this.add(items.formatCurrent());
        }
    }
}
