package cz.zcu.kiwi.shortprocess.model.service;

import cz.zcu.kiwi.shortprocess.model.SQLHelper;

abstract class BaseModelHelper {
    public static final String ID = "id";

    protected final SQLHelper sql;

    BaseModelHelper(SQLHelper sql) {
        this.sql = sql;
    }

}
