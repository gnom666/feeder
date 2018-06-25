package com.taiger.nlp.feeder.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taiger.nlp.feeder.controller.Indexer;
import com.taiger.nlp.feeder.controller.Searcher;
import com.taiger.nlp.feeder.controller.Utils;
import com.taiger.nlp.feeder.model.Configurations;
import com.taiger.nlp.feeder.model.Document4d;
import com.taiger.nlp.feeder.model.LocationNER;
import com.taiger.nlp.feeder.model.PeriodNER;
import com.taiger.nlp.feeder.model.SentenceNER;
import com.taiger.nlp.feeder.model.Text;
import com.taiger.nlp.feeder.repositories.DocumentRepository;

@RestController
@RequestMapping("/indexer")
public class IndexService {

	@Autowired
	private Configurations config;
	
	@Autowired
	private DocumentRepository docRepo;

	@RequestMapping(value="/index", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public Document4d index (@RequestParam(value="id", defaultValue="") String id, @RequestParam(value="text", defaultValue="") String text) {
		Assert.notNull(text, "text shouldn't be null");
		Assert.notNull(id, "id shouldn't be null");
		if (text.trim().isEmpty()) return null;
		
		Document4d doc = new Document4d();
		doc.setSolrId(id);
		
		Text textSentences = config.splitter().detect(text);
		for (String textSentence : textSentences.getSentences()) {
			if (!textSentence.trim().isEmpty()) {
				SentenceNER s;
				s = config.tokenizer().tokenize(textSentence.trim());
				s = config.tagger().annotate(s);
				s = config.dateNER().annotate(s);
				s = config.locationNER().annotate(s);
				Assert.notNull(s, "sentence is null");
				s.getLocations().forEach(doc::addLocation);
				s.getPeriods().forEach(doc::addPeriod);
			}
		}
		
		Indexer indx = new Indexer(docRepo);
		indx.index(doc);
		
		Searcher srch = new Searcher(docRepo);
		doc = null;
		List<Document4d> lst = srch.findBySolrId(id);
		if (lst != null && lst.size() > 0) doc = lst.get(0);
		
		return doc;
    }
	
	@RequestMapping(value="/indexloc", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public List<Document4d> indexLoc (@RequestParam(value="id", defaultValue="") String id, @RequestParam(value="text", defaultValue="") String text) {
		Assert.notNull(text, "text shouldn't be null");
		Assert.notNull(id, "id shouldn't be null");
		if (text.trim().isEmpty()) return null;
		
		Indexer indx = new Indexer(docRepo);
		List<Document4d> deleted = indx.remove(id);
		
		List<LocationNER> locations = new ArrayList<>();
		Set<PeriodNER> periods = new LinkedHashSet<>();
		
		Text textSentences = config.splitter().detect(text);
		for (String textSentence : textSentences.getSentences()) {
			if (!textSentence.trim().isEmpty()) {
				SentenceNER s;
				s = config.tokenizer().tokenize(textSentence.trim());
				s = config.tagger().annotate(s);
				s = config.dateNER().annotate(s);
				s = config.locationNER().annotate(s);
				Assert.notNull(s, "sentence is null");
				s.getLocations().forEach(loc -> Utils.addLocation(locations, loc));
				s.getPeriods().forEach(periods::add);
			}
		}

		locations.forEach (loc -> {
			Document4d doc = new Document4d();
			doc.setSolrId(id);
			doc.setLocations(locations);
			doc.setPeriods(periods);
			doc.setGp(new GeoPoint(loc.getLatitude(), loc.getLongitude()).geohash());
			indx.forcedIndex(doc);
		});
		
		Searcher srch = new Searcher(docRepo);
		
		return srch.findBySolrId(id);
    }
	
	@RequestMapping(value="/delete", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public Document4d delete (@RequestParam(value="id", defaultValue="") String id) {
		Assert.notNull(id, "id shouldn't be null");
		
		Indexer indx = new Indexer(docRepo);
		List<Document4d> deleted = indx.remove(id);
		
		if (deleted != null && deleted.size() > 0) return deleted.get(0);
		return new Document4d();
    }
	
	@RequestMapping(value="/list", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public List<Document4d> list () {
		
		Searcher srch = new Searcher(docRepo);
		
		return srch.findAll();
    }
	
}
