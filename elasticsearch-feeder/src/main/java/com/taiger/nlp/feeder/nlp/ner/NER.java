package com.taiger.nlp.feeder.nlp.ner;

import com.taiger.nlp.feeder.model.SentenceNER;

public interface NER {
	
	SentenceNER annotate (SentenceNER sentence);
	
}
