package com.taiger.nlp.feeder.model.aux;

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
public class DBPediaResource {
	
	@JsonProperty("@URI")
	private String uri;
	
	@JsonProperty("@surfaceForm")
	private String surfaceForm;
	
	@JsonProperty("@support")
	private String support;
	
	@JsonProperty("@types")
	private String types;
	
	@JsonProperty("@offset")
	private String offset;
	
	@JsonProperty("@similarityScore")
	private String similarityScore;
	
	@JsonProperty("@percentageOfSecondRank")
	private String percentageOfSecondRank;
	
}
