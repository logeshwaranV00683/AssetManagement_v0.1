package com.verinite.assetmanagementtool.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "tbl_assets")
@Data
public class AssetsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private int assetId;
    private String assetName;
    private String serialNumber;
    private String empId;
    private String status;
    private String type;
    private LocalDate purchaseDate;
    private LocalDate warrantyDate;
    private String location;
    private String modelName;
    private String operatingSystem;
    private LocalDate returnDate;
    private String addedBy;
    private LocalDate assignedDate;
    private String assignedBy;
    private String assetSourcedBy;
}
