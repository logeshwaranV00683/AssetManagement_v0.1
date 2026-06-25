package com.verinite.assetmanagementtool.controller;

import com.verinite.assetmanagementtool.dto.AdminLoginDto;
import com.verinite.assetmanagementtool.service.serviceImpl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "https://asset-tool.netlify.app", allowedHeaders = "*", allowCredentials = "true")
public class JwtAuthenticationController {
    @Autowired
    AdminServiceImpl service;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> adminLogin(@RequestBody @Valid AdminLoginDto login) throws Exception {
        return service.checkLogin(login);
    }

    @GetMapping("/generate/bcrypt")
    public String generateBcrypt(@RequestParam String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
