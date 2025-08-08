package com.verinite.assetmanagementtool.entity;


import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tbl_assigned_assets")
public class AssignedAssetsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assigned_assets_id")
    private int assignedAssetsId;
    private String assetName;
    private String serialNumber;
    private String empId;
    private String status;
    //private Date returnDate;
    private LocalDate assignedDate;
    private String assignedBy;
    private String type;

}

