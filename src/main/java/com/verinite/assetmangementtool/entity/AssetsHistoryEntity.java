package com.verinite.assetmangementtool.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

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
    private Date returnDate;
    private String empId;
    private Date assignedDate;
    private String assignedBy;


}
