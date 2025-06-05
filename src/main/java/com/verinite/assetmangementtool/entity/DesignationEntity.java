package com.verinite.assetmangementtool.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Entity
@Table(name = "designation")
@Data
public class DesignationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "desc_id", nullable = false)
    private Long descId;
    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z ]{3,26}$", message = "Title  must contains Alphabets only and it should be in the limit of 3 to 12 charectors")

    private String title;
    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z ]{3,12}$", message = "Position name must contains Alphabets only and it should be in the limit of 3 to 12 charectors")

    private String position;



}