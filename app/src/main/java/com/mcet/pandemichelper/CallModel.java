package com.mcet.pandemichelper;

public class CallModel {

    String dept,phn;
    String workerName, location, workerPhone, workName;

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkerPhone() {
        return workerPhone;
    }

    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public CallModel(String workerName, String location, String workerPhone, String workName) {
        this.workerName = workerName;
        this.location = location;
        this.workerPhone = workerPhone;
        this.workName = workName;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getPhn() {
        return phn;
    }

    public void setPhn(String phn) {
        this.phn = phn;
    }

    public CallModel() {
    }

    public CallModel(String dept, String phn) {
        this.dept = dept;
        this.phn = phn;
    }
}
