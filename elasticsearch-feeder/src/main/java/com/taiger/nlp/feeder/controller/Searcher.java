package com.taiger.nlp.feeder.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.taiger.nlp.feeder.model.Document4d;
import com.taiger.nlp.feeder.repositories.DocumentRepository;

public class Searcher {
	
	@Autowired
	private DocumentRepository docRepo;
	
	public Searcher (DocumentRepository docRepo) {
		this.docRepo = docRepo;
	}

	public List<Document4d> findBySolrId (String solrId) {
		Assert.hasText(solrId, "solrId should has text");
		return docRepo.findBySolrId(solrId);
	}
	
	public List<Document4d> findAll () {
		Iterable<Document4d>it = docRepo.findAll();
		List<Document4d> result = new ArrayList<>();
		it.forEach(result::add);
		return result;
	}
}
