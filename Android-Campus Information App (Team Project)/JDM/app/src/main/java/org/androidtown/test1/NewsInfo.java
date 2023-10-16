package org.androidtown.test1;

public class NewsInfo {
    String headline;
    String date;
    int image;

    public NewsInfo(String headline, String date) {
        this.headline = headline;
        this.date = date;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
