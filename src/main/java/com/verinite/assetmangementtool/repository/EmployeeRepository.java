package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, String> {

    List<EmployeeEntity> findByIgnoreCaseStatus(String str);

    List<EmployeeEntity> findByIgnoreCaseLocation(String str);

    EmployeeEntity findByEmpId(String empId);

    List<EmployeeEntity> findAllByOrderByEmpIdDesc();


    EmployeeEntity findTopByOrderByEmpIdDesc();

}