package com.taiger.nlp.feeder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChunkNER {

	private int begin;
	private int size;
	private String text;
	private String link;
	
}
