package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdminRegistrationRepository extends JpaRepository<AdminRegistrationEntity, Long> {
    AdminRegistrationEntity findByEmpIdAndPassword(String empId, String password);

    AdminRegistrationEntity findByEmpId(String empId);
    
    boolean existsByEmpId(String empId);

    void deleteByEmpId(String empId);
}
