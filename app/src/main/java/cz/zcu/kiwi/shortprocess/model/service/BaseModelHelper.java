package cz.zcu.kiwi.shortprocess.model.service;

import java.text.SimpleDateFormat;

import cz.zcu.kiwi.shortprocess.model.SQLHelper;

abstract class BaseModelHelper {
    public static final String ID = "id";

    protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat();

    protected final SQLHelper sql;

    BaseModelHelper(SQLHelper sql) {
        this.sql = sql;
    }

}
