package com.taiger.nlp.feeder.nlp.tagger;

import com.taiger.nlp.feeder.model.SentenceNER;

public interface POSTagger {

	SentenceNER annotate (SentenceNER sentence);
	
}
