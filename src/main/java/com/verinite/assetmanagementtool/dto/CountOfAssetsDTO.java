package com.verinite.assetmanagementtool.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountOfAssetsDTO {

    private Long id;
    private String location;
    private String type;
    private Integer total;
    private Integer Assigned;
    private Integer Unassigned;
    private Integer Scrapped;

    @Override
    public String toString() {
        return "CountOfAssetsDTO{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", total=" + total +
                ", Assigned=" + Assigned +
                ", Unassigned=" + Unassigned +
                ", Scrapped=" + Scrapped +
                '}';
    }
}
