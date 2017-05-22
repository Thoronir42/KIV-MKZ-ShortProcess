package cz.zcu.kiwi.shortprocess.model.entity;


public class BaseEntity {

    protected int _id;

    public BaseEntity() {
        this(-1);
    }

    public BaseEntity(int id) {
        this._id = id;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
}
