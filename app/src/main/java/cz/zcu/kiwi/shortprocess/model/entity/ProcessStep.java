package cz.zcu.kiwi.shortprocess.model.entity;


public class ProcessStep extends BaseEntity {
    private int process_id;
    private int interval_after_start;
    private String caption;
    private String description;

    public ProcessStep(int process_id) {
        this.process_id = process_id;
    }

    public int getProcess_id() {
        return process_id;
    }

    /*public void setProcess_id(int process_id) {
        this.process_id = process_id;
    }*/

    public int getInterval_after_start() {
        return interval_after_start;
    }

    public void setInterval_after_start(int interval_after_start) {
        this.interval_after_start = interval_after_start;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
