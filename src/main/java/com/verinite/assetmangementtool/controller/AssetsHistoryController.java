package com.verinite.assetmangementtool.controller;

import com.verinite.assetmangementtool.entity.AssetsHistoryEntity;
import com.verinite.assetmangementtool.service.AssetsHistoryServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
@RequestMapping("/assetManager/v1/")
public class AssetsHistoryController {
	
	@Autowired
	private AssetsHistoryServices assetsHistoryServices;
	
	@PostMapping("history/save")
	public AssetsHistoryEntity save(@RequestBody AssetsHistoryEntity history) {
		return assetsHistoryServices.saveHistory(history);
	}
	@GetMapping("history/get/all")
	public List<AssetsHistoryEntity> getAll(){
		return assetsHistoryServices.getAll();
	}
//	@GetMapping("histyory/get/by/mail/{mail}")
//	public Object getByMail(@PathVariable String mail) {
//		return assetsHistoryServices.getByMail(mail);
//	}
//	@DeleteMapping("history/delete/by/mail/{mail}")
//	public Object delete(@PathVariable String mail) {
//		return assetsHistoryServices.returnDetails(mail);
//	}
//	@PatchMapping("history/update/by/mail/{mail}")
//	public Object update(@PathVariable String mail,@RequestBody AssetsHistoryEntity history) {
//		return assetsHistoryServices.updateHistory(mail, history);
//	}
		
	@GetMapping("history/get/by/id/{id}")
	public Object getByHisytoryId(@PathVariable long id) {
		return assetsHistoryServices.getByHistoryId(id);
	}
}
