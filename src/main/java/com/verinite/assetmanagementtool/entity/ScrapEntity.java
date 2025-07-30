package com.verinite.assetmanagementtool.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tbl_scraptable")
@Data
public class ScrapEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int scrapId;
    private String Assetname;
    private String SerialNo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date warrantyDate;
    private String users;
    private String status;
    private String type;
    private int assetId;

    public ScrapEntity() {

    }

    public ScrapEntity(int scrapId, String assetname, String serialNo, Date purchaseDate, Date warrantyDate,
                       String users, String status, String type, int assetId) {
        this.scrapId = scrapId;
        Assetname = assetname;
        SerialNo = serialNo;
        this.purchaseDate = purchaseDate;
        this.warrantyDate = warrantyDate;
        this.users = users;
        this.status = status;
        this.type = type;
        this.assetId = assetId;
    }

    @Override
    public String toString() {
        return "ScrapTable{" + "scrapId=" + scrapId + ", Assetname='" + Assetname + '\'' + ", SerialNo='" + SerialNo
                + '\'' + ", purchaseDate=" + purchaseDate + ", warrantyDate=" + warrantyDate + ", Employees='" + users
                + '\'' + ", status='" + status + '\'' + ", type='" + type + '\'' + ", assetId=" + assetId + '}';
    }

}
