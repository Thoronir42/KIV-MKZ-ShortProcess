package cz.zcu.kiwi.shortprocess.model.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;

import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;
import cz.zcu.kiwi.shortprocess.model.entity.BaseEntity;
import cz.zcu.kiwi.shortprocess.model.entity.Process;

abstract class BaseModelHelper<Type extends BaseEntity> {
    public static final String ID = "_id";

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();

    protected final SQLHelper sql;

    BaseModelHelper(SQLHelper sql) {
        this.sql = sql;
    }

    public Type find(int id) {
        SQLiteDatabase db = sql.getReadableDatabase();
        String where = ID +" = ?";
        String[] whereValues = new String[]{"" + id};
        Cursor cursor = db.query(getTable(), null, where, whereValues, null,
                null, null);
        if(!cursor.moveToNext()) {
            return null;
        }

        return (Type)getEntityParser().parse(cursor);
    }

    public ModelCursor<Type> findAll() {
        SQLiteDatabase db = sql.getReadableDatabase();
        Cursor cursor = db.query(getTable(), null, null, null, null,
                null, null);

        //startManagingCursor(cursor);
        return new ModelCursor<>(cursor, getEntityParser());
    }

    protected abstract String getTable();
    protected abstract ModelCursor.Parser getEntityParser();
}
