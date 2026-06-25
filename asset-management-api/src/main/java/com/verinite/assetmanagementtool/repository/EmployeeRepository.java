package com.verinite.assetmanagementtool.repository;

import com.verinite.assetmanagementtool.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, String> {

    List<EmployeeEntity> findByIgnoreCaseStatus(String str);

    List<EmployeeEntity> findByIgnoreCaseLocation(String str);

    EmployeeEntity findByEmpId(String empId);

    List<EmployeeEntity> findAllByOrderByEmpIdDesc();


    EmployeeEntity findTopByOrderByEmpIdDesc();

    boolean existsByMail(String mail);

    boolean existsByMobile(String mobile);

    @Query(value = "SELECT DISTINCT location FROM tbl_employee", nativeQuery = true)
    List<String> getUniqueEmployeeLocation();

    @Query(value = "SELECT DISTINCT designation FROM tbl_employee", nativeQuery = true)
    List<String> getUniqueEmployeeDesignation();

    @Query(value = "SELECT DISTINCT department FROM tbl_employee", nativeQuery = true)
    List<String> getUniqueEmployeeDepartment();
}