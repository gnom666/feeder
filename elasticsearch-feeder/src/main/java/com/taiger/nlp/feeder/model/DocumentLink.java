package com.taiger.nlp.feeder.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.util.Assert;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(indexName = "entity_link")
@Getter
@Setter
@ToString
public class DocumentLink {

	@Id
	@Setter(AccessLevel.NONE)
	private String id;
	private String solrId;
	private List<SentenceNER> text;
	
	
	public DocumentLink() {
		this.id = generateUniqueId();
		this.solrId = "";
		text = new ArrayList<>();
	}
	
	public DocumentLink(String solrId) {
		Assert.notNull(solrId, "id shouldn't be null");
		this.id = generateUniqueId();
		this.solrId = solrId;
		text = new ArrayList<>();
	}
	
	public synchronized String generateUniqueId() {
		return UUID.randomUUID().toString();
	}
	
}
