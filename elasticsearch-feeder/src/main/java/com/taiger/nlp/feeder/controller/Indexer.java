package com.taiger.nlp.feeder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.taiger.nlp.feeder.model.Document4d;
import com.taiger.nlp.feeder.repositories.DocumentRepository;

@Controller
@Component
public class Indexer {
	
	@Autowired
	private DocumentRepository docRepo;
	
	public Indexer (DocumentRepository docRepo) {
		this.docRepo = docRepo;
	}

	public void index (Document4d doc) {
		Assert.notNull(doc, "doc shouldn't be null");
		List<Document4d> docs = docRepo.findBySolrId(doc.getSolrId());
		if (docs.size() == 0) docRepo.save(doc);
	}
	
	public void forcedIndex (Document4d doc) {
		Assert.notNull(doc, "doc shouldn't be null");
		docRepo.save(doc);
	}
	
	public List<Document4d> remove (String solrId) {
		Assert.hasText(solrId, "solrId shouldn't be null");
		List<Document4d> docs = docRepo.removeBySolrId(solrId);
		return docs;
	}
	
}
