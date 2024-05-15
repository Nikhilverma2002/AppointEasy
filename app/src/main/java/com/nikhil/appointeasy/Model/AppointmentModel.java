package com.nikhil.appointeasy.Model;

public class AppointmentModel {

    String name,email,date,time,address,uid,pushkey,doctor;


    public AppointmentModel() {
    }

    public AppointmentModel(String name, String email, String date, String time, String address, String uid, String pushkey, String doctor) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
        this.address = address;
        this.uid = uid;
        this.pushkey = pushkey;
        this.doctor = doctor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }
}
