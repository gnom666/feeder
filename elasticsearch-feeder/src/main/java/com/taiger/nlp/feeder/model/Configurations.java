package com.taiger.nlp.feeder.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.taiger.nlp.feeder.nlp.ner.DateFinderNER;
import com.taiger.nlp.feeder.nlp.ner.LocationFinderNER;
import com.taiger.nlp.feeder.nlp.ner.NER;
import com.taiger.nlp.feeder.nlp.splitter.MESplitter;
import com.taiger.nlp.feeder.nlp.splitter.SentenceSplitter;
import com.taiger.nlp.feeder.nlp.tagger.METagger;
import com.taiger.nlp.feeder.nlp.tagger.POSTagger;
import com.taiger.nlp.feeder.nlp.tokenizer.METokenizer;
import com.taiger.nlp.feeder.nlp.tokenizer.Tokenizer;

@Configuration
public class Configurations {
	
	@Bean
	public SentenceSplitter splitter () { return new MESplitter();}
	
	@Bean
	public NER dateNER () { return new DateFinderNER();}
	
	@Bean
	public NER locationNER () { return new LocationFinderNER();}
	
	@Bean
	public POSTagger tagger () { return new METagger();}
	
	@Bean
	public Tokenizer tokenizer () { return new METokenizer();}
	
}
