package com.touchfuture.takeout.bean;

import java.util.Date;

public class Flight {
    private Integer id;

    private String fltno;

    private String airline;

    private String type;

    private String takeoff;

    private String landing;

    private Date date;

    private Float punctuality;

    private Date takeofftime;

    private Date landingtime;

    private String planeid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFltno() {
        return fltno;
    }

    public void setFltno(String fltno) {
        this.fltno = fltno == null ? null : fltno.trim();
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String ariline) {
        this.airline = ariline;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(String takeoff) {
        this.takeoff = takeoff;
    }

    public String getLanding() {
        return landing;
    }

    public void setLanding(String landing) {
        this.landing = landing;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(Float punctuality) {
        this.punctuality = punctuality;
    }

    public Date getTakeofftime() {
        return takeofftime;
    }

    public void setTakeofftime(Date takeofftime) {
        this.takeofftime = takeofftime;
    }

    public Date getLandingtime() {
        return landingtime;
    }

    public void setLandingtime(Date landingtime) {
        this.landingtime = landingtime;
    }

    public String getPlaneid() {
        return planeid;
    }

    public void setPlaneid(String planeid) {
        this.planeid = planeid == null ? null : planeid.trim();
    }
}