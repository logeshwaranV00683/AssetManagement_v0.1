package com.verinite.assetmanagementtool.entity;

import lombok.Data;

import javax.persistence.*;


@Entity
@Table(name = "tbl_Count")
@Data
public class CountOfAssetsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String location;
    private String type;
    private Integer total;
    private Integer Assigned;
    private Integer Unassigned;
    private Integer Scrapped;

}
