package com.taiger.nlp.feeder.nlp.ner;

import java.io.IOException;
import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.util.Assert;

import com.taiger.nlp.feeder.controller.RestConsumer;
import com.taiger.nlp.feeder.model.Constants;
import com.taiger.nlp.feeder.model.GeoLink;
import com.taiger.nlp.feeder.model.LocationNER;
import com.taiger.nlp.feeder.model.SentenceNER;
import com.taiger.nlp.feeder.model.WordNER;
import com.taiger.nlp.feeder.model.aux.DBPediaLocation;
import com.taiger.nlp.feeder.repositories.GeoLinkRepository;
import com.taiger.nlp.feeder.repositories.LocationRepository;

import lombok.extern.log4j.Log4j2;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;



@Log4j2
public class LocationFinderNER implements NER {

	private NameFinderME nameFinderEn;
	
	private NameFinderME nameFinderEs;
	
	@Autowired
	private LocationRepository locationRepo;
	
	@Autowired
	private GeoLinkRepository geoLinkRepo;
	
	public LocationFinderNER () {
		initialize ();
	}

	public LocationFinderNER initialize () {
		try {
			TokenNameFinderModel lnfModel = new TokenNameFinderModel(LocationFinderNER.class.getClassLoader().getResourceAsStream(Constants.NER_EN_LOCATION));
			this.nameFinderEn = new NameFinderME (lnfModel);
			lnfModel = new TokenNameFinderModel(LocationFinderNER.class.getClassLoader().getResourceAsStream(Constants.NER_ES_LOCATION));
			this.nameFinderEs = new NameFinderME (lnfModel);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}

	@Override
	public SentenceNER annotate(SentenceNER sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getS(), "sentence content shouldn't be null");
		
		//* searching in english
		String[] tokens = new String[sentence.getS().size()];
		for (int i = 0; i < sentence.getS().size(); i++) {
			tokens[i] = sentence.getS().get(i).getW();
		}

		Span[] spansEn = nameFinderEn.find(tokens);
		//analyze (spansEn, tokens);
		for (Span span : spansEn) {
			sentence.getS().get(span.getStart()).setNerTag(Constants.B + Constants.LOCATION);
			sentence.getS().get(span.getStart()).setNerProb(nameFinderEn.probs()[span.getStart()]);
			
			String chunk = Constants.formChunk(tokens, span);
			
			DBPediaLocation dbpl = RestConsumer.dbpediaCall(chunk, "DBpedia:Place,DBpedia:Location");
			Assert.notNull(dbpl, "RestConsumer.dbpediaCall(chunk, \"DBpedia:Place,DBpedia:Location\") NULL");
			if (dbpl.getResources() != null) {
				Pair<Double, Double> coordinates = RestConsumer.dbpediaSparql(dbpl.getResources().get(0).getUri());
			
				LocationNER loc = findLocation(chunk, coordinates);
				if (loc != null) {
					loc.setGeoLink(findGeoLink(chunk));
					sentence.addLocation(loc);
					sentence.getS().get(span.getStart()).setLocation(loc);
					
					for (int i = span.getStart() + 1; i < span.getEnd(); i++) {
						sentence.getS().get(i).setNerTag(Constants.I + Constants.LOCATION);
						sentence.getS().get(i).setNerProb(nameFinderEn.probs()[i]);
					}
				}
			}
			
			
		} //*/
		
		//* Searching in spanish
		int i = 0;
		for (WordNER w : sentence.getS()) {
			boolean condition = w.getNerTag().contains(Constants.B) || w.getNerTag().contains(Constants.I);
			tokens[i] = condition ? "" : w.getW();
			i++;
		}

		Span[] spansEs = nameFinderEs.find(tokens);
		for (Span span : spansEs) {
			sentence.getS().get(span.getStart()).setNerTag(Constants.B + Constants.LOCATION);
			sentence.getS().get(span.getStart()).setNerProb(nameFinderEs.probs()[span.getStart()]);
			
			String chunk = Constants.formChunk(tokens, span);
			
			DBPediaLocation dbpl = RestConsumer.dbpediaCall(chunk, "DBpedia:Place,DBpedia:Location");
			Assert.notNull(dbpl, "RestConsumer.dbpediaCall(chunk, \"DBpedia:Place,DBpedia:Location\") NULL");
			if (dbpl.getResources() != null) {
				Pair<Double, Double> coordinates = RestConsumer.dbpediaSparql(dbpl.getResources().get(0).getUri());
				
				LocationNER loc = findLocation(chunk, coordinates);
				if (loc != null) {
					loc.setGeoLink(findGeoLink(chunk));
					sentence.addLocation(loc);
					sentence.getS().get(span.getStart()).setLocation(loc);
					
					for (i = span.getStart() + 1; i < span.getEnd(); i++) {
						sentence.getS().get(i).setNerTag(Constants.I + Constants.LOCATION);
						sentence.getS().get(i).setNerProb(nameFinderEs.probs()[i]);
					}
				}
			}
		} //*/
		
		return sentence;
	}

	public LocationNER findLocation (String chunk, Pair<Double, Double> coordinates) {
		Assert.notNull(chunk, "got null chunk");
		Assert.notNull(coordinates, "got null coordinates");
		String searchChunk = treat (chunk);
		
		LocationNER loc = null;
		List<LocationNER> locations = locationRepo.findTop100ByCountryNameIgnoreCaseContainingOrCityNameIgnoreCaseContainingOrRegionNameIgnoreCaseContaining(searchChunk, searchChunk, searchChunk);
		//List<LocationNER> locations = locationRepo.findTop100ByCountryNameIgnoreCaseContainingOrCityNameIgnoreCaseContaining(searchChunk, searchChunk);
		
		if (coordinates.getFirst() == 0.0 && coordinates.getSecond() == 0.0) {
			double min = Constants.MAXINT;
			for (int i = 0; i < locations.size(); i++) {
				double d = LevenshteinDistance.getDefaultInstance().apply(chunk, locations.get(i).getCountryName()) * bias (chunk, locations.get(i).getCountryName()) +
						//LevenshteinDistance.getDefaultInstance().apply(chunk, locations.get(i).getRegionName()) * bias (chunk, locations.get(i).getRegionName()) +
						LevenshteinDistance.getDefaultInstance().apply(chunk, locations.get(i).getCityName()) * bias (chunk, locations.get(i).getCityName());
				
				if (LevenshteinDistance.getDefaultInstance().apply(chunk, locations.get(i).getCountryName()) == 0) return locations.get(i);
	
				if (d < min) {
					min = d;
					loc = locations.get(i);
				}
			}
		}	else {
			double min = Double.MAX_VALUE;
			for (int i = 0; i < locations.size(); i++) {
				double d = distance(coordinates.getFirst(), locations.get(i).getLatitude(), 
									coordinates.getSecond(), locations.get(i).getLongitude(),
									0.0, 0.0);
	
				if (d < min) {
					min = d;
					loc = locations.get(i);
				}
			}
		}
		
		return loc;
	}
	
	private String treat (String chunk) {
		return chunk.replace("ö", "oe").replace("ä", "e").replace("ü", "iu");
	}

	public GeoLink findGeoLink (String chunk) {
		Assert.notNull(chunk, "got null chunk");
		String searchChunk = chunk;
		
		GeoLink loc = null;
		List<GeoLink> locations = geoLinkRepo.findByNameIgnoreCaseContaining(searchChunk);
		double min = Constants.MAXINT;
		for (int i = 0; i < locations.size(); i++) {
			double d = LevenshteinDistance.getDefaultInstance().apply(chunk, locations.get(i).getName());

			if (d < min) {
				min = d;
				loc = locations.get(i);
			}
		}
		
		return loc;
	}

	private Double bias(String chunk, String name) {
		if (name.contains(chunk)) return 0.2;
		return 1.0;
	}
	
	private double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

	    final int R = 6371; // Radius of the earth

	    double latDistance = Math.toRadians(lat2 - lat1);
	    double lonDistance = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double distance = R * c * 1000; // convert to meters

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}
	

}
