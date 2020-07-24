package com.embevolter.logtool.model;

import com.embevolter.logtool.Utils;

public class Datetime {
    private String day;
    private String hour;
    private String minute;
    private String second;

    public Datetime(String day, String hour, String minute, String second) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

	@Override
	public String toString() {
        if (Utils.isNullOrEmpty(day) && Utils.isNullOrEmpty(day) 
            && Utils.isNullOrEmpty(day) && Utils.isNullOrEmpty(day)) {
            return "";
        }

		return "Datetime [day=" + day + ", hour=" + hour + ", minute=" + minute + ", second=" + second + "]";
	}

    
}