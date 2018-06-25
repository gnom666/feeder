package com.taiger.nlp.feeder.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taiger.nlp.feeder.model.Configurations;
import com.taiger.nlp.feeder.model.Text;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/splitter")
public class SentenceSplitterServices {
	
	@Autowired
	private Configurations config;

	@RequestMapping(value="/detect", method=RequestMethod.GET, produces="application/json;charset=UTF-8")
    public Text sentenceDetection(@RequestParam(value="text", defaultValue="") String text) {
		Assert.notNull(text, "text shouldn't be null");
		Text result = new Text();
		if (text.trim().isEmpty()) return result;
		
		result = config.splitter().detect(text);
		
		log.info(result.toString());
		
		return result;
    }
}
