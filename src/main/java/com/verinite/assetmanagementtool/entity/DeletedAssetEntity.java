package com.verinite.assetmanagementtool.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "tbl_deleted_asset")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletedAssetEntity {
    @Column(name = "serial_no")
    @Id
    private String serialNo;
    @Column(name = "asset_name")
    private String assetName;
    @Column(name = "purchase_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
    @Column(name = "deleted_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deletedDate;
    private String type;
    private String location;
    @Column(name = "asset_sourced_by")
    private String assetSourcedBy;
    @Column(name = "deleted_by")
    private String deletedBy;

    @Override
    public String toString() {
        return "DeletedAssetEntity{" +
                "serialNo='" + serialNo + '\'' +
                ", assetName='" + assetName + '\'' +
                ", purchaseDate=" + purchaseDate +
                ", deletedDate=" + deletedDate +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", assetSourcedBy='" + assetSourcedBy + '\'' +
                ", deletedBy='" + deletedBy + '\'' +
                '}';
    }
}
