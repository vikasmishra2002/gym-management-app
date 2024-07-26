package com.example.gym_application;

public class Announcement {
    String heading,des;

    public Announcement(String heading, String des) {
        this.heading = heading;
        this.des = des;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
