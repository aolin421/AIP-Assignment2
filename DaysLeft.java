/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package au.edu.uts.aip.ams.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author admin
 */
public class DaysLeft implements Serializable {
    private SimpleDateFormat formatter;
    private Date today;
    private Date dueDate;

    private long millisecond;
    private long second;
    private long minute;
    private long hour;
    private long day;

    public DaysLeft() {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = new Date();
    }

    public DaysLeft(String dueDate) {
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = new Date();
        calDaysLeft(dueDate);
    }

    public void calDaysLeft(String dueDate) {
        try {
            this.dueDate = formatter.parse(dueDate);
            millisecond = (this.dueDate.getTime() - today.getTime());
            second = (millisecond / 1000);
            minute = (second / 60);
            hour = (minute / 60);
            if (hour <= 0) {
                day = (hour / 24);
            } else {
                day = ((hour / 24) + 1);
            }
        } catch (ParseException e) {
            millisecond = 0;
            second = 0;
            minute = 0;
            hour = 0;
            day = 0;
        }
    }

    public void setDateFormat(String dateFormat) {
        formatter = new SimpleDateFormat(dateFormat);
    }

    public void setDueDate(String dueDate) {
        try {
            this.dueDate = formatter.parse(dueDate);
        } catch (ParseException e) {
            this.dueDate = new Date();
        }
    }

    public Date getDueDate() {
        return dueDate;
    }

    public long getLeftMilisecond() {
        return millisecond;
    }

    public long getLeftSecond() {
        return second;
    }

    public long getLeftMinute() {
        return minute;
    }

    public long getLeftHour() {
        return hour;
    }

    public long getLeftDay() {
        return day;
    }
}
