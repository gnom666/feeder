package com.taiger.nlp.feeder.nlp.tokenizer;

import java.io.IOException;

import org.springframework.util.Assert;

import com.taiger.nlp.feeder.model.Constants;
import com.taiger.nlp.feeder.model.SentenceNER;
import com.taiger.nlp.feeder.model.WordNER;

import lombok.extern.log4j.Log4j2;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

@Log4j2
public class METokenizer implements Tokenizer {
	
	private TokenizerME tokenizer;
	
	public METokenizer () {
		initialize ();
	}

	public METokenizer initialize () {
		try {
			TokenizerModel model = new TokenizerModel(METokenizer.class.getClassLoader().getResourceAsStream(Constants.TOKEN_EN));
			this.tokenizer = new TokenizerME(model);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}

	@Override
	public SentenceNER tokenize(String sentence) {
		Assert.hasText(sentence, "sentence should has text");
		
		SentenceNER result = new SentenceNER();
		result.setOriginal(sentence);
	
		String[] otokens = tokenizer.tokenize (sentence);
		double[] tokenProbs = tokenizer.getTokenProbabilities ();

		int min = 0;
		int max = sentence.length();
		for (int i = 0; i < otokens.length; i++) {
			int offset = sentence.substring(min, max).indexOf(otokens[i]) + min;
			min = otokens[i].length() + offset;
			WordNER word = new WordNER (otokens[i], Constants.O, tokenProbs[i], i, offset);
			result.addWord (word);
		}
		
		return result;
	}

}
