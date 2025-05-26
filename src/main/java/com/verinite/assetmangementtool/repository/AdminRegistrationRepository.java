package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.AdminRegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AdminRegistrationRepository extends JpaRepository<AdminRegistrationEntity, Long> {

//    @Query(value = "select mail from tbl_admin", nativeQuery = true)
//    AdminRegistrationEntity findBymail(String email);

    @Query(value = "SELECT * FROM tbl_admin WHERE mail = :mail", nativeQuery = true)
    Optional<AdminRegistrationEntity> findByMail(@Param("mail") String mail);

    AdminRegistrationEntity findByEmpId(String empId);

    boolean existsByEmpId(String empId);

    void deleteByEmpId(String empId);
}
