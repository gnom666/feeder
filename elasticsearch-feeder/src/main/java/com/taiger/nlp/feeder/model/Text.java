package com.taiger.nlp.feeder.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Text {
	
	private List<String> sentences;
	
	public Text() {
		sentences = new ArrayList<>();
	}
	
}
