package com.verinite.assetmangementtool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Data
public class AssignableAssetDto {

    private String empId;
    private String serialNumber;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;
    private String assetName;
    private String assignedBy;
}
