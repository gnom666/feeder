package com.taiger.nlp.feeder.model.aux;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DBPediaLocation {
	
	@JsonProperty("@text")
	private String text;
	
	@JsonProperty("@confidence")
	private String confidence;
	
	@JsonProperty("@support")
	private String support;
	
	@JsonProperty("@types")
	private String types;
	
	@JsonProperty("@sparql")
	private String sparql;
	
	@JsonProperty("@policy")
	private String policy;
	
	@JsonProperty("Resources")
	private List<DBPediaResource> resources;
	
}
