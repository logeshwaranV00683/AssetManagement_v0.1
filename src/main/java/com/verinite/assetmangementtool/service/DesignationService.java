package com.verinite.assetmangementtool.service;


import com.verinite.assetmangementtool.entity.DesignationEntity;

import java.util.List;

public interface DesignationService {
    public DesignationEntity newDesigination(DesignationEntity designation);
    public List<DesignationEntity> getAll();
    public DesignationEntity getByTitle(String title);
    public DesignationEntity getByCode(long code);
    public DesignationEntity getByPosition(String position);
}
