package com.verinite.assetmanagementtool.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class AssetCounterDto {
    private String location;
    private int unAssigned;
    private int total;
}
