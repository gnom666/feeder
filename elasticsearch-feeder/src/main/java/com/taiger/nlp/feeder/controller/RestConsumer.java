package com.taiger.nlp.feeder.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taiger.nlp.feeder.model.SentenceNER;

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
			log.info("Output from Server ....");
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
	
}
