package com.verinite.assetmangementtool.service;

import com.verinite.assetmangementtool.entity.CountOfAssets;
import com.verinite.assetmangementtool.repository.AssetCountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CountOfAssetsImpl implements CountOFAssetsService {
	@Autowired
	AssetCountRepository assectCountRepository;

	@Override
	public int getLaptopCount(String id) {
		try {
			return assectCountRepository.getLaptopCount(id);
		} catch (Exception e) {
			return 0;
		}
		
	}


	@Override
	public CountOfAssets postAssestCount (CountOfAssets countOfAssets) {
		return assectCountRepository.save(countOfAssets);
	}

	@Override
	public Object updateAssetCount(String location, CountOfAssets countOfAssets) {
		try {
			CountOfAssets countOfAssets2= assectCountRepository.findByLocation(location);
			if(countOfAssets.getLaptopCount()!=0)
				countOfAssets2.setLaptopCount(countOfAssets.getLaptopCount());
			if(countOfAssets.getBagCount()!=0)
				countOfAssets2.setBagCount(countOfAssets.getBagCount());
			if(countOfAssets.getLaptopChargerCount()!=0)
				countOfAssets2.setLaptopChargerCount(countOfAssets.getLaptopChargerCount());
			if(countOfAssets.getHeadPhonesCount()!=0)
				countOfAssets2.setHeadPhonesCount(countOfAssets.getHeadPhonesCount());
			if(countOfAssets.getMouseCount()!=0)
				countOfAssets2.setMouseCount(countOfAssets.getMouseCount());
			assectCountRepository.save(countOfAssets2);
			return "Updated Successfully";
		} catch (Exception e) {
			return "No Location is found";
		}
	}

	@Override
	public List<CountOfAssets> getAll() {
		return assectCountRepository.findAll();
	}

	@Override
	public int totalLaptops() {
		List<CountOfAssets> countOfAssets=assectCountRepository.findAll();
		int total=0;
		for(CountOfAssets i : countOfAssets) {
			total+=i.getLaptopCount();
		}
		return total;		
		}
}
