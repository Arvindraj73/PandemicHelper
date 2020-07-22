package com.mcet.pandemichelper;

public class HelpModel {
    String name, symptoms, disease, location, person_count;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPerson_count() {
        return person_count;
    }

    public void setPerson_count(String person_count) {
        this.person_count = person_count;
    }

    public HelpModel() {
    }

    public HelpModel(String name, String symptoms, String disease, String location) {
        this.name = name;
        this.symptoms = symptoms;
        this.disease = disease;
        this.location = location;
    }

    public HelpModel(String name, String location, String person_count) {
        this.name = name;
        this.location = location;
        this.person_count = person_count;
    }
}
