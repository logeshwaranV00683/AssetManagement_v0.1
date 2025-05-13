package com.verinite.assetmangementtool.service;


import com.verinite.assetmangementtool.entity.ScrapEntity;
import com.verinite.assetmangementtool.repository.ScarpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

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

        }catch (Exception e){
            return scrapId+"not found";
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
        }catch (Exception e){
            return"not found"+ scrapId;
        }
    }

//    public static void main(String[] args) throws ParseException {
//
//        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse("1999-06-03 05:30:00");
//
//        System.out.println(new Date().toInstant());
//        System.out.println((DAYS.between(date1.toInstant(),new Date().toInstant()))>15);
//    }
}
