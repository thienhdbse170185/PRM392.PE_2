package com.example.studentmanagement.model;

import java.io.Serializable;

public class Student implements Serializable {
    private String id;
    private String name;
    private String date;
    private boolean gender;
    private String email;
    private String address;
    private String majorId;
    private Major major;

    public Student(String id, String name, String date, boolean gender, String email, String address, String majorId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.gender = gender;
        this.email = email;
        this.address = address;
        this.majorId = majorId;
    }

    public Student(String name, String date, boolean gender, String email, String address, String majorId) {
        this.name = name;
        this.date = date;
        this.gender = gender;
        this.email = email;
        this.address = address;
        this.majorId = majorId;
    }

    public Student(String id, String name, String formattedDate, boolean gender, String email, String address, String majorId, Major major) {
        this.id = id;
        this.name = name;
        this.date = formattedDate;
        this.gender = gender;
        this.email = email;
        this.address = address;
        this.majorId = majorId;
        this.major = major;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public Major getMajor() {return major;}

    public void setMajor(Major major) {this.major = major;}
}