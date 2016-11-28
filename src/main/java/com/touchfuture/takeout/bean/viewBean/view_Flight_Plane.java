package com.touchfuture.takeout.bean.viewBean;

import com.touchfuture.takeout.bean.Plane;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by user on 2016/11/28.
 */
public class view_Flight_Plane {

    private Integer id;

    private String fltno;

    private String airline;

    private String takeoff;

    private String landing;

    private Date date;

    private Float punctuality;

    private Time takeofftime;

    private Time landingtime;

    private String type;

    private String ptype;

    private Float score;

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
        this.fltno = fltno;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
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

    public Time getTakeofftime() {
        return takeofftime;
    }

    public void setTakeofftime(Time takeofftime) {
        this.takeofftime = takeofftime;
    }

    public Time getLandingtime() {
        return landingtime;
    }

    public void setLandingtime(Time landingtime) {
        this.landingtime = landingtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }



}
