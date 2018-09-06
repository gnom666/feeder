package com.taiger.nlp.feeder.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taiger.nlp.feeder.controller.Indexer;
import com.taiger.nlp.feeder.controller.Searcher;
import com.taiger.nlp.feeder.controller.Utils;
import com.taiger.nlp.feeder.model.Configurations;
import com.taiger.nlp.feeder.model.Document4d;
import com.taiger.nlp.feeder.model.DocumentLink;
import com.taiger.nlp.feeder.model.LocationNER;
import com.taiger.nlp.feeder.model.PeriodNER;
import com.taiger.nlp.feeder.model.SentenceNER;
import com.taiger.nlp.feeder.model.Text;
import com.taiger.nlp.feeder.repositories.DocumentLinkRepository;
import com.taiger.nlp.feeder.repositories.DocumentRepository;

@RestController
@RequestMapping("/indexer")
public class IndexService {

	@Autowired
	private Configurations config;
	
	@Autowired
	private DocumentRepository docRepo;
	
	@Autowired
	private DocumentLinkRepository docLinkRepo;

	/*@Deprecated
	@RequestMapping(value="/index", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public Document4d index (@RequestParam(value="id", defaultValue="") String id, @RequestParam(value="text", defaultValue="") String text) {
		Assert.notNull(text, "text shouldn't be null");
		Assert.notNull(id, "id shouldn't be null");
		if (text.trim().isEmpty()) return null;
		
		Document4d doc = new Document4d(id);
		
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
		
		Indexer indx = new Indexer(docRepo, docLinkRepo);
		indx.index(doc);
		
		Searcher srch = new Searcher(docRepo, docLinkRepo);
		doc = null;
		List<Document4d> lst = srch.findBySolrId(id);
		if (lst != null && lst.size() > 0) doc = lst.get(0);
		
		return doc;
    }*/
	
	@RequestMapping(value="/index", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
    public List<Document4d> indexPost (@RequestParam(value="id", defaultValue="") String id, @RequestBody String text) {
		Assert.notNull(text, "text shouldn't be null");
		Assert.notNull(id, "id shouldn't be null");
		if (text.trim().isEmpty()) return null;
		
		Indexer indx = new Indexer(docRepo, docLinkRepo);
		indx.remove4dDoc(id);
		indx.removeLinkDoc(id);
		
		List<LocationNER> locations = new ArrayList<>();
		Set<PeriodNER> periods = new LinkedHashSet<>();
		
		DocumentLink docLink = new DocumentLink(id);
		
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
				
				docLink.getText().add(s);
			}
		}
		
		indx.index(docLink);

		locations.forEach (loc -> {
			Document4d doc = new Document4d(id);
			doc.setLocation(loc);
			doc.setPeriods(periods);
			doc.setGp(new GeoPoint(loc.getLatitude(), loc.getLongitude()).geohash());
			indx.forcedIndex(doc);
		});
		
		Searcher srch = new Searcher(docRepo, docLinkRepo);
		
		return srch.findBySolrId(id);
	}
	
	@RequestMapping(value="/indexloc", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public List<Document4d> indexLoc (@RequestParam(value="id", defaultValue="") String id, @RequestParam(value="text", defaultValue="") String text) {
		Assert.notNull(text, "text shouldn't be null");
		Assert.notNull(id, "id shouldn't be null");
		if (text.trim().isEmpty()) return null;
		
		Indexer indx = new Indexer(docRepo, docLinkRepo);
		indx.remove4dDoc(id);
		indx.removeLinkDoc(id);
		
		List<LocationNER> locations = new ArrayList<>();
		Set<PeriodNER> periods = new LinkedHashSet<>();
		
		DocumentLink docLink = new DocumentLink(id);
		
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
				
				docLink.getText().add(s);
			}
		}
		
		indx.index(docLink);

		locations.forEach (loc -> {
			Document4d doc = new Document4d(id);
			doc.setLocation(loc);
			doc.setPeriods(periods);
			doc.setGp(new GeoPoint(loc.getLatitude(), loc.getLongitude()).geohash());
			indx.forcedIndex(doc);
		});
		
		Searcher srch = new Searcher(docRepo, docLinkRepo);
		
		return srch.findBySolrId(id);
    }
	
	@RequestMapping(value="/delete", method=RequestMethod.GET, produces="application/text;charset=UTF-8")
    public String delete (@RequestParam(value="id", defaultValue="") String id) {
		Assert.notNull(id, "id shouldn't be null");
		
		Indexer indx = new Indexer(docRepo, docLinkRepo);
		indx.remove4dDoc(id);
		indx.removeLinkDoc(id);
		
		return "OK";
    }
	
	@RequestMapping(value="/list4d", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public List<Document4d> list4d () {
		
		Searcher srch = new Searcher(docRepo, docLinkRepo);
		
		return srch.findAll4d();
    }
	
	@RequestMapping(value="/listld", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public List<DocumentLink> listLd () {
		
		Searcher srch = new Searcher(docRepo, docLinkRepo);
		
		return srch.findAllLd();
    }
	
}
