package com.verinite.assetmanagementtool.dto;

import lombok.Data;

@Data
public class AssetCounterDto {
    private String location;
    private int unAssigned;
    private int total;
}
