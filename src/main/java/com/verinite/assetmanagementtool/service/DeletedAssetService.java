package com.verinite.assetmanagementtool.service;


import com.verinite.assetmanagementtool.dto.DeletedAssetDto;

import java.util.List;

public interface DeletedAssetService {

    List<DeletedAssetDto> getAllDeleted();

    void permananteDelete(String serialNo, String adminId);
}
