package com.taiger.nlp.feeder.repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.taiger.nlp.feeder.model.GeoLink;

public interface GeoLinkRepository extends PagingAndSortingRepository<GeoLink, Long> {

	List<GeoLink> findByNameIgnoreCase (String name);
	
	List<GeoLink> findTop10ByNameIgnoreCaseContaining(String name);
	
	List<GeoLink> findTop100ByNameIgnoreCaseContaining(String name);
	
	List<GeoLink> findByNameIgnoreCaseContaining(String name);
	
}
