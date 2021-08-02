package com.example.raju.safalmadmin;

import android.app.Application;

public class SafalmAdmin extends Application {

    private String empId;

    public String getempId() {
        return empId;
    }

    public void setempId(String empId) {
        this.empId = empId;
    }
}

