package com.clevmania.medbay.model;

/**
 * Created by grandilo-lawrence on 4/8/18.
 */

public class MedicationsModel {
    private String title;
    private String desc;
    private String interval;
    private String start_date;
    private String end_date;
    private String dosage;

    public MedicationsModel(String title, String desc, String interval, String start_date, String end_date, String dosage) {
        this.title = title;
        this.desc = desc;
        this.interval = interval;

        this.start_date = start_date;
        this.end_date = end_date;
        this.dosage = dosage;
    }

    public MedicationsModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
}
