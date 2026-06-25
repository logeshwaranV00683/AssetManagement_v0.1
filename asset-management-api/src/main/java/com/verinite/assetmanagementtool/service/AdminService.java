package com.verinite.assetmanagementtool.service;

import com.verinite.assetmanagementtool.dto.AdminLoginDto;
import com.verinite.assetmanagementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmanagementtool.entity.EmployeeEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    Object registerNewAdmin(AdminRegistrationEntity registration);

    List<AdminRegistrationEntity> getAll();

    ResponseEntity<?> checkLogin(AdminLoginDto login) throws Exception;

    void updateAdminEntity(EmployeeEntity existingEmployee);
}
