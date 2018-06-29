package com.taiger.nlp.feeder.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.taiger.nlp.feeder.model.Document4d;
import com.taiger.nlp.feeder.model.DocumentLink;
import com.taiger.nlp.feeder.repositories.DocumentLinkRepository;
import com.taiger.nlp.feeder.repositories.DocumentRepository;

public class Searcher {

	private DocumentRepository docRepo;
	private DocumentLinkRepository docLinkRepo;
	
	public Searcher (DocumentRepository docRepo, DocumentLinkRepository docLinkRepo) {
		Assert.notNull(docRepo, "repo shouldn't be null");
		Assert.notNull(docLinkRepo, "link repo shouldn't be null");
		this.docRepo = docRepo;
		this.docLinkRepo = docLinkRepo;
	}

	public List<Document4d> findBySolrId (String solrId) {
		Assert.hasText(solrId, "solrId should has text");
		return docRepo.findBySolrId(solrId);
	}
	
	public List<Document4d> findAll4d () {
		Iterable<Document4d>it = docRepo.findAll();
		List<Document4d> result = new ArrayList<>();
		it.forEach(result::add);
		return result;
	}
	
	public List<DocumentLink> findAllLd () {
		Iterable<DocumentLink>it = docLinkRepo.findAll();
		List<DocumentLink> result = new ArrayList<>();
		it.forEach(result::add);
		return result;
	}
}
