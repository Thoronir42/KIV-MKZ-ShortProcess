package cz.zcu.kiwi.shortprocess.model.entity;


import java.util.Date;

public class ProcessRunStep extends BaseEntity {

    private final long processRunId;
    private final long processStepId;
    private Date notifiedOn;
    private int status;

    public ProcessRunStep(long processRunId, long processStepId) {
        this.processRunId = processRunId;
        this.processStepId = processStepId;
    }

    public long getProcessRunId() {
        return processRunId;
    }

    public long getProcessStepId() {
        return processStepId;
    }

    public Date getNotifiedOn() {
        return notifiedOn;
    }

    public void setNotifiedOn(Date notifiedOn) {
        this.notifiedOn = notifiedOn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
