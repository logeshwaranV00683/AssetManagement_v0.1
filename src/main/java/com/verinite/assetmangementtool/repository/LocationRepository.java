package com.verinite.assetmangementtool.repository;

import com.verinite.assetmangementtool.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Number> {

	List<Object> findByState(String locName);

	List<Object> findByCountry(String countryName);
	

}
