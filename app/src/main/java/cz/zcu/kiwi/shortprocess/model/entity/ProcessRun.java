package cz.zcu.kiwi.shortprocess.model.entity;


import java.util.Date;

public class ProcessRun extends BaseEntity {

    private long process_id;
    private Date date_started;
    private Date date_finished;

    public ProcessRun(long processId, Date dateStarted) {

    }

    public long getProcessId() {
        return process_id;
    }


    public Date getDateStarted() {
        return date_started;
    }

    public void setDateStarted(Date dateStarted) {
        this.date_started = dateStarted;
    }

    public Date getDateFinished() {
        return date_finished;
    }

    public void setDateFinished(Date date_finished) {
        this.date_finished = date_finished;
    }
}
