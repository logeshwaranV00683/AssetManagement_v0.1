package com.verinite.assetmangementtool.service;


import com.verinite.assetmangementtool.entity.ScrapEntity;

import java.util.List;

public interface ScrapService {
    ScrapEntity postScrap(ScrapEntity scrapTable);

    List<ScrapEntity> getScrap();

    Object getScrapById(int scrapId);

    Object ScrapPut(ScrapEntity scrapTable, int scrapId);
}
