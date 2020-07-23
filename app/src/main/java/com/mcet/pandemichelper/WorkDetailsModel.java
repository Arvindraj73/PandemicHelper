package com.mcet.pandemichelper;

public class WorkDetailsModel {

    String Name;
    String Description;
    String PhoneNumber;
    String NoOfWorkers;
    String Status;
    String lat;
    String lon;
    String key;
    String location;
    int i;
    String assignedTo;

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public WorkDetailsModel(String name, String lat, String lon) {
        Name = name;
        this.lat = lat;
        this.lon = lon;

    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public WorkDetailsModel(String name, String key) {
        Name = name;
        this.key = key;
    }

    public WorkDetailsModel(String name, String phoneNumber, String lat, String lon) {
        Name = name;
        PhoneNumber = phoneNumber;
        this.lat = lat;
        this.lon = lon;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getNoOfWorkers() {
        return NoOfWorkers;
    }

    public void setNoOfWorkers(String noOfWorkers) {
        NoOfWorkers = noOfWorkers;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public WorkDetailsModel() {
    }

    public WorkDetailsModel(String name, String description, String phoneNumber, String lat, String lon) {
        Name = name;
        Description = description;
        PhoneNumber = phoneNumber;
        this.lat = lat;
        this.lon = lon;
    }

    public WorkDetailsModel(String name, String description, String phoneNumber, String noOfWorkers, String status, String lat, String lon) {
        Name = name;
        Description = description;
        PhoneNumber = phoneNumber;
        NoOfWorkers = noOfWorkers;
        Status = status;
        this.lat = lat;
        this.lon = lon;
    }

    public WorkDetailsModel(String name, String description, String phoneNumber, String noOfWorkers, String assignedTo, String lat, String lon, int i) {
        Name = name;
        Description = description;
        PhoneNumber = phoneNumber;
        NoOfWorkers = noOfWorkers;
        this.assignedTo = assignedTo;
        this.lat = lat;
        this.lon = lon;
        this.i = i;
    }
}
