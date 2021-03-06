package com.taiger.nlp.feeder.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodNER {
	
	private HistoricalPeriod period;
	private String name;
	private int year;
	private String link;
	
	
}
