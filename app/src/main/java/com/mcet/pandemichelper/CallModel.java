package com.mcet.pandemichelper;

public class CallModel {

    String dept,phn;

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
