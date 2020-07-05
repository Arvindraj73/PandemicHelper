package com.mcet.pandemichelper;

public class MaterialModel {
    String uid;
    String itemName;
    String quantity;
    String name,lon,lat;
    String total;

    public String getTotal() {
        return total;
    }

    public MaterialModel(String name, String lon, String lat, String total) {
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        this.total = total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public MaterialModel(String itemName, String quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
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
