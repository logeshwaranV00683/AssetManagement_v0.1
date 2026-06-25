package com.verinite.assetmanagementtool.repository;

import com.verinite.assetmanagementtool.entity.AdminRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdminRegistrationRepository extends JpaRepository<AdminRegistrationEntity, Long> {

    @Query(value = "SELECT * FROM tbl_admin WHERE mail = :mail", nativeQuery = true)
    Optional<AdminRegistrationEntity> findByMail(@Param("mail") String mail);

    AdminRegistrationEntity findByEmpId(String empId);

    boolean existsByEmpId(String empId);

    void deleteByEmpId(String empId);
}
