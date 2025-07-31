package com.verinite.assetmanagementtool.service.serviceImpl;


import com.verinite.assetmanagementtool.entity.ScrapEntity;
import com.verinite.assetmanagementtool.repository.ScarpRepository;
import com.verinite.assetmanagementtool.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScrapServiceImpl implements ScrapService {
    @Autowired
    ScrapService scrapService;
    @Autowired
    ScarpRepository scarpRepository;

    @Override
    public ScrapEntity postScrap(ScrapEntity scrapTable) {
        return scarpRepository.save(scrapTable);
    }

    @Override
    public List<ScrapEntity> getScrap() {
        return scarpRepository.findAll();
    }

    @Override
    public Object getScrapById(int scrapId) {
        try {
            return scarpRepository.findById(scrapId).get();

        } catch (Exception e) {
            return scrapId + "not found";
        }
    }

    @Override
    public Object ScrapPut(ScrapEntity scrapTable, int scrapId) {
        try {
            ScrapEntity scrapTable1 = scarpRepository.findById(scrapId).get();
            scrapTable1.setAssetname(scrapTable.getAssetname());
            scrapTable1.setUsers(scrapTable.getUsers());
            scrapTable1.setType(scrapTable.getType());
            return scarpRepository.save(scrapTable1);
        } catch (Exception e) {
            return "not found" + scrapId;
        }
    }

}
