package com.mcet.pandemichelper;

public class PassDetailsModel {

    String name, age, gender, reason, idType, idNumber, reasonProof,
            idProof, fromAddress, toAddress, noOfPassengers,
            vehicleType, vehicleNo, travelDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getReasonProof() {
        return reasonProof;
    }

    public void setReasonProof(String reasonProof) {
        this.reasonProof = reasonProof;
    }

    public String getIdProof() {
        return idProof;
    }

    public void setIdProof(String idProof) {
        this.idProof = idProof;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getNoOfPassengers() {
        return noOfPassengers;
    }

    public void setNoOfPassengers(String noOfPassengers) {
        this.noOfPassengers = noOfPassengers;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(String travelDate) {
        this.travelDate = travelDate;
    }

    public PassDetailsModel() {
    }

    public PassDetailsModel(String name, String age, String gender,
                            String reason, String idType, String idNumber,
                            String fromAddress, String toAddress, String noOfPassengers,
                            String vehicleType, String vehicleNo, String travelDate) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.reason = reason;
        this.idType = idType;
        this.idNumber = idNumber;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.noOfPassengers = noOfPassengers;
        this.vehicleType = vehicleType;
        this.vehicleNo = vehicleNo;
        this.travelDate = travelDate;
    }
}
