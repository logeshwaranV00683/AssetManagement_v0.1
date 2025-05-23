package com.verinite.assetmangementtool.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class AssignableAssetDto {

    private String empId;
    private String serialNumber;
    private Date assignedDate;
    private String assetName;
    private String assignedBy;
}
