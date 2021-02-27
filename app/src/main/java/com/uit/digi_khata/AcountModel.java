package com.uit.digi_khata;

public class AcountModel
{
    String date,time,gave,got ;

    public AcountModel() {
    }

    public AcountModel(String date, String time, String gave, String got) {
        this.date = date;
        this.time = time;
        this.gave = gave;
        this.got = got;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGave() {
        return gave;
    }

    public void setGave(String gave) {
        this.gave = gave;
    }

    public String getGot() {
        return got;
    }

    public void setGot(String got) {
        this.got = got;
    }


}
