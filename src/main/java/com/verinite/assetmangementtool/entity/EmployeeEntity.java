package com.verinite.assetmangementtool.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "tbl_employee")
@Data
public class EmployeeEntity {
    @Id
    @Column(name = "emp_id")
    private String empId;

    private String firstName;

    private String lastName;

    private String role;

    private String mail;

    private String mobile;

    private String location;

    private String status;

    private String department;

    private String designation;
    @Lob
    @Column(name = "picture", columnDefinition = "LONGBLOB")
    private Blob picture;


}