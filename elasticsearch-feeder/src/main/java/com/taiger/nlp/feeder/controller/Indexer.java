package com.taiger.nlp.feeder.controller;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import com.taiger.nlp.feeder.model.Document4d;
import com.taiger.nlp.feeder.model.DocumentLink;
import com.taiger.nlp.feeder.repositories.DocumentLinkRepository;
import com.taiger.nlp.feeder.repositories.DocumentRepository;

@Controller
@Component
public class Indexer {

	private DocumentRepository docRepo;
	private DocumentLinkRepository docLinkRepo;
	
	public Indexer (DocumentRepository docRepo, DocumentLinkRepository docLinkRepo) {
		Assert.notNull(docRepo, "repo shouldn't be null");
		Assert.notNull(docLinkRepo, "link repo shouldn't be null");
		this.docRepo = docRepo;
		this.docLinkRepo = docLinkRepo;
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
	
	public List<Document4d> remove4dDoc (String solrId) {
		Assert.hasText(solrId, "solrId shouldn't be null");
		List<Document4d> docs = docRepo.removeBySolrId(solrId);
		return docs;
	}
	
	
	public void index (DocumentLink doc) {
		Assert.notNull(doc, "doc shouldn't be null");
		List<DocumentLink> docs = docLinkRepo.findBySolrId(doc.getSolrId());
		if (docs.size() == 0) docLinkRepo.save(doc);
	}
	
	public void forcedIndex (DocumentLink doc) {
		Assert.notNull(doc, "doc shouldn't be null");
		docLinkRepo.save(doc);
	}
	
	public List<DocumentLink> removeLinkDoc (String solrId) {
		Assert.hasText(solrId, "solrId shouldn't be null");
		List<DocumentLink> docs = docLinkRepo.removeBySolrId(solrId);
		return docs;
	}
	
}
