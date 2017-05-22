package cz.zcu.kiwi.shortprocess.model;

import android.database.Cursor;
import android.database.CursorWrapper;

public class ModelCursor<E> extends CursorWrapper {

    private Parser parser;

    public ModelCursor(Cursor cursor, Parser parser) {
        super(cursor);
        this.parser = parser;
    }

    public E formatCurrent() {
        return (E)parser.parse(this);
    }

    public static abstract class Parser {
        public abstract Object parse(Cursor c);
    }
}
