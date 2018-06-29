package com.taiger.nlp.feeder.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.taiger.nlp.feeder.model.LocationNER;



public interface LocationRepository extends PagingAndSortingRepository<LocationNER, Integer> {
	
	List<LocationNER> findTop3ByCountryNameIgnoreCaseContaining(String countryName);
	
	List<LocationNER> findTop5ByCountryNameIgnoreCaseContaining(String countryName);
		
	List<LocationNER> findTop3ByRegionNameIgnoreCaseContaining(String regionName);
	
	List<LocationNER> findTop3ByCityNameIgnoreCaseContaining(String cityName);
	
	List<LocationNER> findTop4ByCityNameIgnoreCaseContaining(String cityName);
	
	List<LocationNER> findTop100ByCountryNameIgnoreCaseContainingOrCityNameIgnoreCaseContainingOrRegionNameIgnoreCaseContaining(String countryName, String cityName, String regionName);
	
	List<LocationNER> findTopByCountryNameIgnoreCaseContainingOrCityNameIgnoreCaseContainingOrRegionNameIgnoreCaseContaining(String countryName, String cityName, String regionName);
	
	List<LocationNER> findTop100ByCountryNameIgnoreCaseContainingOrCityNameIgnoreCaseContaining(String countryName, String cityName);
	
	//List<Location> findREgistries();
	
}
