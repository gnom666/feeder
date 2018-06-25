package com.taiger.nlp.feeder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taiger.nlp.feeder.model.Configurations;
import com.taiger.nlp.feeder.model.SentenceNER;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/ner")
public class NERServices {
	
	@Autowired
	private Configurations config;

	@RequestMapping(value="/annotate", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public SentenceNER nerAnnotations(@RequestParam(value="sentence", defaultValue="") String sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		SentenceNER result = new SentenceNER();
		if (sentence.trim().isEmpty()) return result;
		
		result = config.tokenizer().tokenize(sentence.trim());
		result = config.tagger().annotate(result);
		result = config.dateNER().annotate(result);
		result = config.locationNER().annotate(result);
		
		log.info(result.toString());
		
		return result;
    }
	
}
