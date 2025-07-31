package com.verinite.assetmanagementtool.dto;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tbl_asset_names")
public class AssetNameDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long assetNameId;
    private String assetName;

}
