package com.embevolter.logtool.model;

public class Datetime {
    private int day;
    private int hour;
    private int minute;
    private int second;

    public Datetime(int day, int hour, int minute, int second) {
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

	@Override
	public String toString() {
		return "Datetime [day=" + day + ", hour=" + hour + ", minute=" + minute + ", second=" + second + "]";
	}

    
}