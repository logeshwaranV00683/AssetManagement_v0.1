package com.verinite.assetmangementtool.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

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
    private Date assignedDate;
    private String assignedBy;
}
