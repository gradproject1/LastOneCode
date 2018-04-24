package com.example.user1.urnextapp;

// class to store patient information for accept page
public class Accept_List_Information {


    private String name;
    private String time;
    private String arrival;

    public Accept_List_Information(){
        //this constructor is required
    }

    public Accept_List_Information(String name, String time, String arrival) {
        this.name = name;
        this.time = time;
        this.arrival =arrival;
    }

    String getname() {
        return name;
    }

    String gettime() {
        return time;
    }

    String getarrival(){return arrival;}
}

