package cz.zcu.kiwi.shortprocess.model.entity;


import android.support.annotation.NonNull;

public class ProcessStep extends BaseEntity {
    private int process_id;
    private int interval_after_start;
    private String caption;
    private String description;

    public ProcessStep(int process_id) {
        this.process_id = process_id;
    }

    public int getProcessId() {
        return process_id;
    }

    /*public void setProcess_id(int process_id) {
        this.process_id = process_id;
    }*/

    public int getIntervalAfterStart() {
        return interval_after_start;
    }

    public void setIntervalAfterStart(int interval_after_start) {
        this.interval_after_start = interval_after_start;
    }

    @NonNull
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption != null ? caption : "";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
