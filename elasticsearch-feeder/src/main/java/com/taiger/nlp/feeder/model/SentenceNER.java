package com.taiger.nlp.feeder.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SentenceNER {

	private String original;
	private List<WordNER> s;
	private List<LocationNER> locations;
	private Set<PeriodNER> periods;
	
	public SentenceNER () {
		s = new ArrayList<>();
		original = "";
		locations = new LinkedList<>();
		periods = new LinkedHashSet<>();
	}
	
	public void addWord (WordNER word) {
		Assert.notNull(word, "word should not be null");
		s.add(word);
	}
	
	public void addLocation (LocationNER location) {
		if (location == null) return;
		if (!locationExists(location)) this.locations.add(location);
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
