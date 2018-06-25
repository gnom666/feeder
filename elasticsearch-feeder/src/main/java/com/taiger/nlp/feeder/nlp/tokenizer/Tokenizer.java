package com.taiger.nlp.feeder.nlp.tokenizer;

import com.taiger.nlp.feeder.model.SentenceNER;

public interface Tokenizer {

	SentenceNER tokenize (String sentence);
	
}
