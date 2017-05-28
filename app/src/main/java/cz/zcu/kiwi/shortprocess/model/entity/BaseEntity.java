package cz.zcu.kiwi.shortprocess.model.entity;


public class BaseEntity {

    protected long _id;

    public BaseEntity() {
        this(-1);
    }

    public BaseEntity(int id) {
        this._id = id;
    }

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }
}
