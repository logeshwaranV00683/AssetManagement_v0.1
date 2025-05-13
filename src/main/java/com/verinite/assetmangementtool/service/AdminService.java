package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AdminLoginDto;
import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    public Object registerNewAdmin(AdminRegistrationEntity registration);
    public List<AdminRegistrationEntity> getAll();

    ResponseEntity checkLogin(AdminLoginDto login) throws Exception;
}
