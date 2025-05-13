package com.verinite.assetmangementtool.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "tbl_asset_names")
public class AssetNameDTO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long assetNameId;
	private String assetName;

	public Long getAssetNameId() {
		return assetNameId;
	}

	public void setAssetNameId(Long assetNameId) {
		this.assetNameId = assetNameId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

}
