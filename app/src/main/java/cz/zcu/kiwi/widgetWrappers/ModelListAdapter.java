package cz.zcu.kiwi.widgetWrappers;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.entity.BaseEntity;

public class ModelListAdapter<Type extends BaseEntity> extends ArrayAdapter<Type> {

    protected final LayoutInflater inflater;
    protected EntityClickListener<Type> onClick;

    public void setOnClick(EntityClickListener<Type> onClick) {

        this.onClick = onClick;
    }

    public ModelListAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(ModelCursor<Type> items) {
        this.clear();
        Log.v("ProcessListAdapter", "Displaying " + items.getCount() + " entities");

        while (items.moveToNext()) {
            this.add(items.formatCurrent());
        }
    }

}
