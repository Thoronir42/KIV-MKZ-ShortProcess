package cz.zcu.kiwi.shortprocess.model.entity;

import java.util.Date;

public class Process extends BaseEntity {
    private String title;
    private String description;

    private Date date_created;

    public Process(String title, long milliseconds) {
        this(title, new Date(milliseconds));
    }

    public Process(String title, Date date_created) {
        this.title = title;
        this.date_created = date_created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return date_created;
    }

    /*public void setDateCreated(Date date_created) {
        this.date_created = date_created;
    }*/
}
