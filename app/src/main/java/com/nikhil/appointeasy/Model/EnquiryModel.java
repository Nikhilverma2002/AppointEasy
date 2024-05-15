package com.nikhil.appointeasy.Model;

public class EnquiryModel {

    String address , date, doctor,email,name,pushkey,time, uid,enquiry;


    public EnquiryModel() {
    }

    public EnquiryModel(String address, String date, String doctor, String email, String name, String pushkey, String time, String uid, String enquiry) {
        this.address = address;
        this.date = date;
        this.doctor = doctor;
        this.email = email;
        this.name = name;
        this.pushkey = pushkey;
        this.time = time;
        this.uid = uid;
        this.enquiry = enquiry;
    }

    public String getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(String enquiry) {
        this.enquiry = enquiry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
