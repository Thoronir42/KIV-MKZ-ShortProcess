package cz.zcu.kiwi.shortprocess.model;

import android.database.Cursor;


public abstract class EntityParser<E> {
    public abstract E parse(Cursor c);
}
