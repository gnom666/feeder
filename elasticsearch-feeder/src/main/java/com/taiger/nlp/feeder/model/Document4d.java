package com.taiger.nlp.feeder.model;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

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
	private LocationNER location;
	@GeoPointField
	private String gp;
	private Set<PeriodNER> periods;
	
	public Document4d() {
		this.id = generateUniqueId();
		this.solrId = "";
		this.description = "";
		this.periods = new LinkedHashSet<>();
	}
	
	public Document4d(String solrId) {
		Assert.notNull(solrId, "id shouldn't be null");
		this.id = generateUniqueId();
		this.solrId = solrId;
		this.description = "";
		this.periods = new LinkedHashSet<>();
	}
	
	public synchronized String generateUniqueId() {
		return UUID.randomUUID().toString();
	}
	
	public void addLocation (LocationNER location) {
		this.location = location;
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
			if (it.next().getPeriod().name().equals(period.getPeriod().name())) found = true;
		}
		return found;
	}
	
}
