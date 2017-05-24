package cz.zcu.kiwi.shortprocess.model;

import android.content.ContentValues;
import android.database.Cursor;


public abstract class EntityParser<E> {
    public abstract E parse(Cursor c);

    public abstract ContentValues parse(E entity);
}
