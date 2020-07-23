package com.mcet.pandemichelper;

public class EssentialPassModel {
    String applicantName, applicantProofType, applicantProofNo, applicantProof;
    String orName;
    String orType;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String orCategory;
    String orProofType;
    String orProofNo;
    String status;

    public EssentialPassModel() {
    }

    String orProof;
    String orAddress;
    String vehicleCount;
    String vehicleNo, vehicleType;

    public EssentialPassModel(String vehicleType, String vehicleNo) {
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
    }

    public EssentialPassModel(String applicantName, String applicantProofType, String applicantProofNo, String orName, String orType,
                              String orCategory, String orProofType, String orProofNo, String orAddress, String vehicleCount, String status) {
        this.applicantName = applicantName;
        this.applicantProofType = applicantProofType;
        this.applicantProofNo = applicantProofNo;
        this.orName = orName;
        this.orType = orType;
        this.orCategory = orCategory;
        this.orProofType = orProofType;
        this.orProofNo = orProofNo;
        this.orAddress = orAddress;
        this.vehicleCount = vehicleCount;
        this.status = status;
    }

    public String getOrAddress() {
        return orAddress;
    }

    public void setOrAddress(String orAddress) {
        this.orAddress = orAddress;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantProofType() {
        return applicantProofType;
    }

    public void setApplicantProofType(String applicantProofType) {
        this.applicantProofType = applicantProofType;
    }

    public String getApplicantProofNo() {
        return applicantProofNo;
    }

    public void setApplicantProofNo(String applicantProofNo) {
        this.applicantProofNo = applicantProofNo;
    }

    public String getApplicantProof() {
        return applicantProof;
    }

    public void setApplicantProof(String applicantProof) {
        this.applicantProof = applicantProof;
    }

    public String getOrName() {
        return orName;
    }

    public void setOrName(String orName) {
        this.orName = orName;
    }

    public String getOrType() {
        return orType;
    }

    public void setOrType(String orType) {
        this.orType = orType;
    }

    public String getOrCategory() {
        return orCategory;
    }

    public void setOrCategory(String orCategory) {
        this.orCategory = orCategory;
    }

    public String getOrProofType() {
        return orProofType;
    }

    public void setOrProofType(String orProofType) {
        this.orProofType = orProofType;
    }

    public String getOrProofNo() {
        return orProofNo;
    }

    public void setOrProofNo(String orProofNo) {
        this.orProofNo = orProofNo;
    }

    public String getOrProof() {
        return orProof;
    }

    public void setOrProof(String orProof) {
        this.orProof = orProof;
    }

    public String getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(String vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
