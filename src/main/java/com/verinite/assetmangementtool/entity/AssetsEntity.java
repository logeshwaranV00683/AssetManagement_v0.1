package com.verinite.assetmangementtool.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "tbl_assets")
@Data
public class AssetsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "asset_id")
    private int assetId;
    private String assetName;
    private String serialNumber;
    private String empId;
    private String status;
    private String type;
    private String purchaseDate;
    private String warrantyDate;
    private String location;
    private Integer locCode;
    private String modelName;
    private String operatingSystem;
    private Date returnDate;
    private String addedBy;
    private Date assignedDate;
    private String assignedBy;
    private String assertSourcedBy;
}
