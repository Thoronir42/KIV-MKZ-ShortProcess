package cz.zcu.kiwi.shortprocess.model.entity;

import java.util.Date;

public class Process {
    private String title;
    private String description;

    private Date date_created;

    private int total_executions;
    private int running_executions;

    public Process(String title, Date date_created) {
        this(title, date_created, 0, 0);
    }

    public Process(String title, Date date_created, int total_executions, int running_executions) {
        this.title = title;
        this.date_created = date_created;
        this.total_executions = total_executions;
        this.running_executions = running_executions;
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

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public int getTotal_executions() {
        return total_executions;
    }

    public void setTotal_executions(int total_executions) {
        this.total_executions = total_executions;
    }

    public int getRunning_executions() {
        return running_executions;
    }

    public void setRunning_executions(int running_executions) {
        this.running_executions = running_executions;
    }
}
