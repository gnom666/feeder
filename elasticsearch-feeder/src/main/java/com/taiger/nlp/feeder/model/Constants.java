package com.taiger.nlp.feeder.model;

import lombok.Getter;
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
	
	public static final String DBPEDIA_EN_URL = "https://api.dbpedia-spotlight.org/en/annotate?";
	public static final String DBPEDIA_SNORQL = "http://dbpedia.org/sparql";
	public static final String DBPEDIA_ES_URL = "https://api.dbpedia-spotlight.org/es/annotate?";
	public static final String TEXT_PARAM = "text=";
	public static final String TYPES_PARAM = "types=";
	
	public static final int MAXINT = 999999;
	
	@Getter
	public static enum RESOURCE {
		LAT ("http://www.w3.org/2003/01/geo/wgs84_pos#lat"),
		LATITUDE ("http://dbpedia.org/property/latitude"),
		LONG ("http://www.w3.org/2003/01/geo/wgs84_pos#long"),
		LONGITUDE ("http://dbpedia.org/property/longitude"),
		GEOPOINT ("http://www.georss.org/georss/point");
		
		private final String link;
		
		private RESOURCE (String link) {
			this.link = link;
		}
	}
	
	public static String formChunk (String [] tokens, Span sp) {
		StringBuilder chunk = new StringBuilder();
		
		for (int i = sp.getStart(); i < sp.getEnd(); i++) {
			chunk.append(tokens[i] + " ");
		}
		
		return chunk.toString().trim();
	}
	
}
