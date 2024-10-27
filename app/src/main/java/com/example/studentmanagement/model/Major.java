package com.example.studentmanagement.model;

public class Major {
    private String id;
    private String name;

    public Major(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Major() {

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
}
