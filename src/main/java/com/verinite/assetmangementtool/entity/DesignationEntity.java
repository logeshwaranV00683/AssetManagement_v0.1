package com.verinite.assetmangementtool.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Entity
@Table(name = "designation")
public class DesignationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "desc_id", nullable = false)
    private Long descId;
    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z ]{3,26}$", message = "Title  must contains Alphabets only and it should be in the limit of 3 to 12 charectors")

    private String title;
    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z ]{3,12}$", message = "Position name must contains Alphabets only and it should be in the limit of 3 to 12 charectors")

    private String position;
	public Long getDescId() {
		return descId;
	}
	public void setDescId(Long descId) {
		this.descId = descId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
    
    

}