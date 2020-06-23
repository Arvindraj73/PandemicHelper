package com.mcet.pandemichelper;

public class MaterialModel {
    String uid;
    String itemName;
    String quantity;

    public MaterialModel(String itemName, String quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public MaterialModel() {
    }

    public MaterialModel(String uid, String itemName, String quantity) {
        this.uid = uid;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
