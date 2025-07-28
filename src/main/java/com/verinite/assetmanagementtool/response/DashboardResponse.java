package com.verinite.assetmanagementtool.response;

import com.verinite.assetmanagementtool.entity.AssetsEntity;
import com.verinite.assetmanagementtool.entity.LocationEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DashboardResponse {
    List<AssetsEntity> assetsEntities;
    List<LocationEntity> locationEntities;
}
