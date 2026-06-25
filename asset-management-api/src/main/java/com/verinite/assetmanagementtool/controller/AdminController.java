package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmanagementtool.service.serviceImpl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/assetManager/v1/admin")
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
public class AdminController {
    @Autowired
    AdminServiceImpl service;

    @PostMapping("/add/admin")
    public Object registerAdmin(@Valid @RequestBody AdminRegistrationEntity adminData) {
        return service.registerNewAdmin(adminData);
    }

    @GetMapping("/get/all")
    public List<AdminRegistrationEntity> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/delete/{empId}")
    public Object delete(String empId) {
        return service.deleteAdmin(empId);

    }

}
