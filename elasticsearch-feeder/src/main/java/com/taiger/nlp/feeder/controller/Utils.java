package com.taiger.nlp.feeder.controller;

import java.util.Iterator;
import java.util.List;

import org.springframework.util.Assert;

import com.taiger.nlp.feeder.model.LocationNER;

public class Utils {
	
	public static void addLocation (List<LocationNER> locations, LocationNER location) {
		if (location == null) return;
		if (!locationExists(locations, location)) {
			locations.add(location);
		}
	}
	
	private static boolean locationExists (List<LocationNER> locations, LocationNER location) {
		Assert.notNull(location, "location shouldn't be null");
		boolean found = false;
		Iterator<LocationNER> it = locations.iterator();
		while (it.hasNext() && !found) {
			if (it.next().equal(location)) found = true;
		}
		return found;
	}
}
