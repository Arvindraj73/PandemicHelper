package com.mcet.pandemichelper;

public class UserModel {

    String Name,Phone,Email,Pass,Uid,Role,lat,lon;

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

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public UserModel() {
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public UserModel(String name, String phone, String email, String pass, String uid, String role, String lat, String lon) {
        Name = name;
        Phone = phone;
        Email = email;
        Pass = pass;
        Uid = uid;
        Role = role;
        this.lat = lat;
        this.lon = lon;
    }
}
