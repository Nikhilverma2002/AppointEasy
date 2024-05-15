package com.nikhil.appointeasy.Model;

public class DoctorModel {
    String date, name, about,amount,experience,contact ,location,appointments,pushkey,education;

    public DoctorModel() {
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getAmount() {
        return amount;
    }

    public String getExperience() {
        return experience;
    }

    public String getContact() {
        return contact;
    }

    public String getLocation() {
        return location;
    }

    public String getAppointments() {
        return appointments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAppointments(String appointments) {
        this.appointments = appointments;
    }
}
