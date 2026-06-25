package com.verinite.assetmanagementtool.model;

public class LaptopStatus {
    private Integer id;
    private String statusName;

    public LaptopStatus() {
        super();
    }

    public LaptopStatus(Integer id, String statusName) {
        super();
        this.id = id;
        this.statusName = statusName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
