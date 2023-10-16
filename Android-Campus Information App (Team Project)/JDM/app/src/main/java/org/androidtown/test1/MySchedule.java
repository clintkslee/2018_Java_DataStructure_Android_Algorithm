package org.androidtown.test1;

public class MySchedule {
    String date,title,color;
    public MySchedule(String date,String title,String color)
    {
        this.date = date;
        this.title = title;
        this.color = color;
    }
    public String getDate()
    {
        return date;
    }
    public String getTitle()
    {
        return title;
    }
    public String getColor()
    {
        return color;
    }
}
