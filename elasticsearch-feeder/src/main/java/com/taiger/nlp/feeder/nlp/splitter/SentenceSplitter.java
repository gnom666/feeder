package com.taiger.nlp.feeder.nlp.splitter;

import com.taiger.nlp.feeder.model.Text;

public interface SentenceSplitter {
	
	Text detect (String text);
	
}
