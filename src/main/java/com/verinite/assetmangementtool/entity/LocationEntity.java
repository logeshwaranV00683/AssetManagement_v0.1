package com.verinite.assetmangementtool.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "tbl_location")
@Data
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_code", nullable = true)
    //@Pattern(regexp = "^[0-9]{4}$", message = "Contains Pincode only digits")
    private int locCode;


    //	@Pattern(regexp = "^[A-Z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{1,30}$", message = "Start with Captial letter.Location Name must be of 2 to 50 length with no special characters , symbols and digits")
//	@Column(name = "loc_name",nullable = false)	
//	@NotBlank(message = "Location name should not be null")
    private String locName;

    //	@Pattern(regexp = "^[A-Z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{1,30}$", message = "Start with Captial letter.State must be of 2 to 50 length with no special characters , symbols and digits")
//	@NotBlank(message = "State name should not be null")
    @Column(name = "state", nullable = false)
    private String state;


    //	@Pattern(regexp = "^[A-Z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{1,30}$", message = "Start with Captial letter.Country must be of 2 to 50 length with no special characters , symbols and digits")
//	@NotBlank(message = "Country name should not be null")
    @Column(name = "country", nullable = false)
    private String country;

}
