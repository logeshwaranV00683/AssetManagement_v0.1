package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.LocationEntity;
import com.verinite.assetmangementtool.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;



@Service
public class LocationServiceImpl implements LocationService {
	@Autowired
	LocationRepository locationRepo;

	@Override
	public LocationEntity saveLocation(LocationEntity location) {
		return locationRepo.save(location);
	}

	@Override
	public Object getLocatioById(int id) {
		try {
			return locationRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			return ("Id not found");
		}
		
	}

	@Override
	public List<Object> getByStateName(String locName) {
	
			return locationRepo.findByState(locName);
			
		
		
	}
	@Override
	public Object updateLocation(int id,LocationEntity location) {
		try {
			System.out.println(location);
			LocationEntity location1=locationRepo.findById(id).get();
			if(location.getLocName()!=null)
			location1.setLocName(location.getLocName());
			if(location.getCountry()!=null)
			location1.setCountry(location.getCountry());
			if(location.getState()!=null)
			location1.setState(location.getState());
			
			return locationRepo.save(location1);
		} catch (NoSuchElementException e) {
			return "Id not found";
		}
	}

	@Override
	public List<Object> getByCountry(String countryName) {
		
			return locationRepo.findByCountry(countryName);
	}

	@Override
	public List<LocationEntity> getAllLocations() {
		return locationRepo.findAll();
	}

}
