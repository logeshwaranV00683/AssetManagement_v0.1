package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.CountOfAssets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface AssetCountRepository extends JpaRepository<CountOfAssets, String> {

    @Query(value = "SELECT laptop_count FROM tbl_count WHERE location = ?1", nativeQuery = true)
    int getLaptopCount(String location);

    CountOfAssets findByLocation(String location);
    // @Query(value = "SELECT * FROM tbl_count WHERE location = ?1", nativeQuery = true)

    @Query(value = "SELECT bag_count, camera_count, data_card_count, dvr_count, fire_wall_count, head_phones_count, laptop_charger_count, laptop_count, mobile_count, mouse_count, projector_count, speaker_count, switch_count FROM tbl_count WHERE location = ?1", nativeQuery = true)
    List<Object[]> findByLoc(String location);

    @Query(value = "SELECT un_assigned_bag_count, un_assigned_camera_count, un_assigned_data_card_count, un_assigned_dvr_count, un_assigned_fire_wall_count, un_assigned_headphones_count, un_assigned_laptop_charger_count, un_assigned_laptop_count, un_assigned_mobile_count, un_assigned_mouse_count, un_assigned_projector_count, un_assigned_speaker_count, un_assigned_switch_count FROM tbl_count WHERE location = ?1", nativeQuery = true)
    List<Object[]> findUnassignedAssets(String location);

    @Query(value = "SELECT assigned_bag_count, assigned_camera_count, assigned_data_card_count, assigned_dvr_count, assigned_fire_wall_count, assigned_headphones_count, assigned_laptop_charger_count, assigned_laptop_count, assigned_mobile_count, assigned_mouse_count, assigned_projector_count, assigned_speaker_count, assigned_switch_count FROM tbl_count WHERE location = ?1", nativeQuery = true)
    List<Object[]> findAssignedAssets(String location);

    //CountOfAssets findByAssertSourcedBy(String name);
}
