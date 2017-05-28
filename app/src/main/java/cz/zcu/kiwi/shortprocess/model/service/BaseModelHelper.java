package cz.zcu.kiwi.shortprocess.model.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import cz.zcu.kiwi.shortprocess.model.EntityParser;
import cz.zcu.kiwi.shortprocess.model.ModelCursor;
import cz.zcu.kiwi.shortprocess.model.SQLHelper;
import cz.zcu.kiwi.shortprocess.model.entity.BaseEntity;

abstract class BaseModelHelper<Type extends BaseEntity> {
    public static final String ID = "_id";

    protected final SQLHelper sql;

    BaseModelHelper(SQLHelper sql) {
        this.sql = sql;
    }

    @Nullable
    public Type find(long id) {
        SQLiteDatabase db = sql.getReadableDatabase();
        String where = ID + " = ?";
        String[] whereValues = new String[]{"" + id};
        Cursor cursor = db.query(getTable(), null, where, whereValues, null,
                null, null);
        if (!cursor.moveToNext()) {
            return null;
        }

        return getEntityParser().parse(cursor);
    }

    @NonNull
    public ModelCursor<Type> findAll() {
        SQLiteDatabase db = sql.getReadableDatabase();
        Cursor cursor = db.query(getTable(), null, null, null, null,
                null, null);

        //startManagingCursor(cursor);
        return new ModelCursor<>(cursor, getEntityParser());
    }

    public long insert(@NonNull Type entity) {
        if (this.find(entity.getId()) != null) {
            Log.e("BaseModelHelper", "Insert failed: Entity with id " + entity.getId() + " already exists");
            return -1;
        }

        SQLiteDatabase db = this.sql.getWritableDatabase();
        long result = db.insert(getTable(), null, getEntityParser().parse(entity));
        db.close();

        if (result != -1) {
            Log.i("BaseModelHelper", "Entity of type " + getType(entity) + "inserted with " +
                    "_id = " + result);
            entity.setId(result);
        }
        return result;
    }

    public int update(@NonNull Type entity) {
        SQLiteDatabase db = this.sql.getWritableDatabase();
        if (this.find(entity.getId()) == null) {
            Log.e("BaseModelHelper", "Update failed: Entity with id " + entity.getId() + " does not exist");
            return -1;
        }

        String[] whereArgs = new String[]{"" + entity.getId()};
        int result = db.update(getTable(), this.getEntityParser().parse(entity), ID + " = ?", whereArgs);
        db.close();

        if (result > 0) {
            Log.i("BaseModelHelper", "Updated " + result + " entities of type " + getType(entity));
        }
        return result;
    }

    public int delete(@NonNull Type entity) {
        if (this.find(entity.getId()) == null) {
            Log.e("BaseModelHelper", "Delete failed: Entity with id " + entity.getId() + " does not exist");
            return -1;
        }

        SQLiteDatabase db = this.sql.getWritableDatabase();
        String[] whereArgs = new String[]{"" + entity.getId()};
        int result = db.delete(getTable(), ID + " = ?", whereArgs);
        db.close();

        if (result > 0) {
            Log.i("BaseModelHelper", "Deleted " + result + " entities of type " + getType(entity));
        }
        return result;
    }

    protected abstract String getTable();

    protected abstract EntityParser<Type> getEntityParser();

    private String getType(Type t) {
        if(t == null) {
            return "null";
        }
        return t.getClass().getSimpleName();
    }
}
