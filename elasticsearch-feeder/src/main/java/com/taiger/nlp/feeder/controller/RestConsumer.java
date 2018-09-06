package com.taiger.nlp.feeder.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.data.util.Pair;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taiger.nlp.feeder.model.Constants;
import com.taiger.nlp.feeder.model.SentenceNER;
import com.taiger.nlp.feeder.model.aux.DBPediaLocation;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RestConsumer {
	
	public static SentenceNER nerCall (String urlText, String paramName, String paramContent) {
		Assert.hasText(urlText, "urlText should has text");
		Assert.hasText(paramName, "paramName should has text");
		Assert.hasText(paramContent, "paramContent should has text");
		ObjectMapper mapper = new ObjectMapper();
		SentenceNER sentence = new SentenceNER();
		
		try {

			URL url = new URL(urlText + paramName + URLEncoder.encode(paramContent, "UTF-8"));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			
			String output;
			StringBuilder sb = new StringBuilder();
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
		
			conn.disconnect();
			
			sentence = mapper.readValue(sb.toString(), SentenceNER.class);
		
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}

		return sentence;
	}
	
	public static DBPediaLocation dbpediaCall (String text, String types) {
		Assert.hasText(text, "text should has text");
		Assert.hasText(types, "types should has text");
		StringBuilder sb = new StringBuilder();
		ObjectMapper mapper = new ObjectMapper();
		DBPediaLocation dbpl = new DBPediaLocation();
		
		log.info(text);
		log.info(types);
		
		try {

			URL url = new URL(Constants.DBPEDIA_EN_URL 
							+ Constants.TEXT_PARAM + URLEncoder.encode(text, "UTF-8")
			 				+ "&" + Constants.TYPES_PARAM + URLEncoder.encode(types, "UTF-8"));
			log.info(url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			if (conn.getResponseCode() != 200) {
				if (conn.getResponseCode() == 502 && conn.getResponseCode() != 200) 
					throw new RuntimeException("Failed twice : HTTP error code : " + conn.getResponseCode());
				else
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			
			String output = "";
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
		
			conn.disconnect();
			
			dbpl = mapper.readValue(sb.toString(), DBPediaLocation.class);
		
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}

		return dbpl;
	}
	
	public static Pair<Double, Double> dbpediaSparql (String locationUrl) {

		String sparqlQueryString = "SELECT * WHERE { <" + locationUrl + "> ?p ?o . }";

		Query query = QueryFactory.create(sparqlQueryString);
		QueryExecution qexec =   QueryExecutionFactory.sparqlService(Constants.DBPEDIA_SNORQL, query);
		
		Double lat = 0.0;
		Double lon = 0.0;
		
		try {
		    ResultSet results = qexec.execSelect();
		    
		    for ( ; results.hasNext() ; ) {
		        QuerySolution soln = results.nextSolution() ;           
		        
		        if (soln.get("?p").toString().contains(Constants.RESOURCE.GEOPOINT.getLink()) && soln.get("?o").toString().contains(" ")) {
		        	lat = extractDouble(soln.get("?o").toString().split(" ")[0]); 
		        	lon = extractDouble(soln.get("?o").toString().split(" ")[1]);
		        	return Pair.of(lat, lon);
		        }
		        
		        if (soln.get("?p").toString().contains(Constants.RESOURCE.LAT.getLink())) {
		        	lat = extractDouble(soln.get("?o").toString());
		        }
		        if (soln.get("?p").toString().contains(Constants.RESOURCE.LONG.getLink())) {
		        	lon = extractDouble(soln.get("?o").toString() );
		        }
		    }
        }	catch (Exception e) {
            log.error(e.getMessage());
        }	finally { 
        	qexec.close(); 
        }
		
		return Pair.of(lat, lon);
	}
	
	private static double extractDouble (String value) {
		StringBuilder str = new StringBuilder();
		Assert.hasText(value, "value should have text");
		for (int i = 0; i < value.length() && (StringUtils.isNumeric(String.valueOf(value.charAt(i))) || value.charAt(i) == '.' || value.charAt(i) == '-'); i++) {
			str.append(value.charAt(i));
		}
		if (str.length() > 0)
			return Double.parseDouble(str.toString());
		return Double.parseDouble("0.0");
	}

	
}
