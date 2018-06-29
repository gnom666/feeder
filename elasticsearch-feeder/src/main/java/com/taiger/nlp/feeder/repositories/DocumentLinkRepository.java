package com.taiger.nlp.feeder.repositories;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.transaction.annotation.Transactional;

import com.taiger.nlp.feeder.model.DocumentLink;

public interface DocumentLinkRepository extends ElasticsearchRepository<DocumentLink, String> {

    List<DocumentLink> findBySolrId (String solrId);
    
    @Transactional
	List<DocumentLink> removeBySolrId(String solrId);

    //@Query("{\"bool\" : {\"must\" : {\"term\" : {\"message\" : \"?0\"}}}}")
}
