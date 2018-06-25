package com.taiger.nlp.feeder.repositories;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.transaction.annotation.Transactional;

import com.taiger.nlp.feeder.model.Document4d;

public interface DocumentRepository extends ElasticsearchRepository<Document4d, String> {

    List<Document4d> findBySolrId (String solrId);
    
    @Transactional
	List<Document4d> removeBySolrId(String solrId);

    //@Query("{\"bool\" : {\"must\" : {\"term\" : {\"message\" : \"?0\"}}}}")
}
