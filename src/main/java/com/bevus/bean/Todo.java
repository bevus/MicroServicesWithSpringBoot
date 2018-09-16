package com.bevus.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Todo {
    private int id;
    private String user;
    private String desc;
    private Date targetDate;
    @JsonProperty("done")
    private Boolean isDone;

    public Todo(int id, String user, String desc, Date targetDate, Boolean isDone) {
        this.id = id;
        this.user = user;
        this.desc = desc;
        this.targetDate = targetDate;
        this.isDone = isDone;
    }

    public Todo() {
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getDesc() {
        return desc;
    }

    public Date getTargetDate() {
        return targetDate;
    }

    public Boolean getDone() {
        return isDone;
    }
}