package cz.zcu.kiwi.shortprocess.model;

import android.database.Cursor;
import android.database.CursorWrapper;

public class ModelCursor<E> extends CursorWrapper {

    private EntityParser<E> parser;

    public ModelCursor(Cursor cursor, EntityParser<E> parser) {
        super(cursor);
        this.parser = parser;
    }

    public E formatCurrent() {
        return parser.parse(this);
    }

}
