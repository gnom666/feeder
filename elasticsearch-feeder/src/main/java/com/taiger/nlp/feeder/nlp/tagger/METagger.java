package com.taiger.nlp.feeder.nlp.tagger;

import java.io.IOException;

import org.springframework.util.Assert;

import com.taiger.nlp.feeder.model.Constants;
import com.taiger.nlp.feeder.model.SentenceNER;

import lombok.extern.log4j.Log4j2;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

@Log4j2
public class METagger implements POSTagger {

	private POSTaggerME tagger;
	
	public METagger () {
		initialize  ();
	}

	public METagger initialize () {
		try {
			POSModel model = new POSModel (METagger.class.getClassLoader().getResourceAsStream(Constants.TAGGER_EN));
			this.tagger = new POSTaggerME (model);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}
	
	@Override
	public SentenceNER annotate(SentenceNER sentence) {
		Assert.notNull(sentence, "sentence shouldn't be null");
		Assert.notNull(sentence.getS(), "sentence content shouldn't be null");
		String[] tokens = new String[sentence.getS().size()];
		for (int i = 0; i < sentence.getS().size(); i++) {
			tokens[i] = sentence.getS().get(i).getW();
		}
		
		String[] tags = tagger.tag(tokens);
        for (int i = 0; i < tokens.length; i++) {
        	sentence.getS().get(i).setPosTag(tags[i]);
        }
		
		return sentence;
	}

}
