package com.taiger.nlp.feeder.model;

import opennlp.tools.util.Span;

public class Constants {

	public static final String B = "B-";
	public static final String I = "I-";
	public static final String O = "O";
	
	public static final String LOCATION = "LOCATION";
	public static final String DATE = "DATE";
	
	public static final String NER_ES_DATE = "models/es-ner-misc.bin";
	public static final String NER_EN_DATE = "models/en-ner-date.bin";
	public static final String NER_ES_LOCATION = "models/es-ner-location.bin";
	public static final String NER_EN_LOCATION = "models/en-ner-location.bin";
	public static final String TOKEN_EN = "models/en-token.bin";
	public static final String TAGGER_EN = "models/en-pos-maxent.bin";
	public static final String SENTENCE_EN = "models/en-sent.bin";
	
	public static final int MAXINT = 999999;
	
	public static String formChunk (String [] tokens, Span sp) {
		StringBuilder chunk = new StringBuilder();
		
		for (int i = sp.getStart(); i < sp.getEnd(); i++) {
			chunk.append(tokens[i] + " ");
		}
		
		return chunk.toString().trim();
	}
	
}
