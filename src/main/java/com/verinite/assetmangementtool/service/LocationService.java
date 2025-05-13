package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.LocationEntity;

import java.util.List;



public interface LocationService {
	public LocationEntity saveLocation(LocationEntity location);
	public Object getLocatioById(int id);
	public List<Object> getByStateName(String locName);
	public List<Object> getByCountry(String countryName);
	public List<LocationEntity> getAllLocations();
//	public Object delteLocationByName(String locName);
//	public Object deleteByState(String stateName);
//	public Object deleteByCountry(String countryName);
	public Object updateLocation(int id,LocationEntity location);
//	public Object deleteByID(int id);
	
	
	

}
