package com.verinite.assetmangementtool.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_assets_history")
public class AssetsHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long historyId;
    private String serialNumber;
    private LocalDate returnDate;
    private String empId;
    private LocalDate assignedDate;
    private String assignedBy;


}
