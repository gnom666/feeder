package com.taiger.nlp.feeder.model;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum HistoricalPeriod {
	PREHISTORY ("https://en.wikipedia.org/wiki/Prehistory"),
	ANCIENT_AGE ("https://en.wikipedia.org/wiki/Ancient_history"),
	MIDDLE_AGE ("https://en.wikipedia.org/wiki/Middle_Ages"),
	LATE_MIDDLE_AGE ("https://en.wikipedia.org/wiki/Late_Middle_Ages"), // includes XV
	MODERN_HISTORY_XVI ("https://en.wikipedia.org/wiki/Early_modern_period"), // includes XV
	MODERN_HISTORY_XVII ("https://en.wikipedia.org/wiki/Modern_history#Late_modern_period_2"), // includes XVIII
	CONTEMPORARY_HISTORY_XIX ("https://en.wikipedia.org/wiki/Contemporary_history"), // includes XVIII
	CONTEMPORARY_HISTORY_XX ("https://en.wikipedia.org/wiki/Contemporary_history"),
	CONTEMPORARY_HISTORY_XXI ("https://en.wikipedia.org/wiki/Contemporary_history"),
	UNKNOWN ("");
	
	private final String link;
	
	private HistoricalPeriod (String link) {
		this.link = link;
	}
	
}
