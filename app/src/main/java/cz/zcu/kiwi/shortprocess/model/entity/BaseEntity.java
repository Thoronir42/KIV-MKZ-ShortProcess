package cz.zcu.kiwi.shortprocess.model.entity;


import android.content.ContentValues;
import android.support.annotation.NonNull;

public class BaseEntity {

    protected long _id;

    private final ContentValues extras;

    public BaseEntity() {
        this(-1);
    }

    public BaseEntity(int id) {
        this._id = id;
        this.extras = new ContentValues();
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    @NonNull
    public ContentValues extras() {
        return extras;
    }
}
