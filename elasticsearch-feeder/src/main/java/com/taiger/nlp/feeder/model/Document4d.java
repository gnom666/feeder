package com.taiger.nlp.feeder.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(indexName = "entity4d")
@Getter
@Setter
@ToString
public class Document4d {

	@Id
	@Setter(AccessLevel.NONE)
	private String id;
	private String solrId;
	private String description;
	private List<LocationNER> locations;
	@GeoPointField
	private String gp;
	private Set<PeriodNER> periods;
	
	public Document4d() {
		this.id = generateUniqueId();
		this.solrId = "";
		this.description = "";
		this.locations = new ArrayList<>();
		this.periods = new LinkedHashSet<>();
	}
	
	public synchronized String generateUniqueId() {
		return UUID.randomUUID().toString();
	}
	
	public void addLocation (LocationNER location) {
		if (location == null) return;
		if (!locationExists(location)) {
			gp = new GeoPoint(location.getLatitude(), location.getLongitude()).getGeohash();
			this.locations.add(location);
		}
	}
	
	private boolean locationExists (LocationNER location) {
		Assert.notNull(location, "location shouldn't be null");
		boolean found = false;
		Iterator<LocationNER> it = this.locations.iterator();
		while (it.hasNext() && !found) {
			if (it.next().equal(location)) found = true;
		}
		return found;
	}
	
	public void addPeriod (PeriodNER period) {
		if (period == null) return;
		if (!periodExists(period)) this.periods.add(period);
	}
	
	private boolean periodExists (PeriodNER period) {
		Assert.notNull(period, "period shouldn't be null");
		boolean found = false;
		Iterator<PeriodNER> it = this.periods.iterator();
		while (it.hasNext() && !found) {
			if (it.next().equals(period)) found = true;
		}
		return found;
	}
	
}
