package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.dto.AdminLoginDto;
import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import com.verinite.assetmangementtool.entity.EmployeeEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    Object registerNewAdmin(AdminRegistrationEntity registration);

    List<AdminRegistrationEntity> getAll();

    ResponseEntity<?> checkLogin(AdminLoginDto login) throws Exception;

    ResponseEntity<?> updateAdminEntity(EmployeeEntity existingEmployee);
}
