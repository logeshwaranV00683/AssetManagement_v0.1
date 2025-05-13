package com.verinite.assetmangementtool.controller;


import com.verinite.assetmangementtool.entity.ScrapEntity;
import com.verinite.assetmangementtool.service.ScrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scrap")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ScrapController {
    //Logger logger = LoggerFactory.getLogger(ScrapController.class);
    
    @Autowired
    private ScrapService scrapService;
    
    @PostMapping("/scrappost")
    public ScrapEntity postScrap(@RequestBody ScrapEntity scrapTable){
        //logger.info("debug");
        return scrapService.postScrap(scrapTable);
    }
    @GetMapping("/scrapgetall")
    public List<ScrapEntity> getScrap(){
       // logger.info("debug");
        return scrapService.getScrap();
    }
    @GetMapping("/scrapget/{scrapId}")
    public Object getScrapById(@PathVariable int scrapId){
        //logger.info("debug");
      return scrapService.getScrapById(scrapId);
    }
    @PutMapping("/scrapput/{scrapId}")
    public Object ScrapPut(@RequestBody ScrapEntity scrapTable,@PathVariable int scrapId){
       // logger.info("debug");
        return scrapService.ScrapPut(scrapTable,scrapId);
    }
}
