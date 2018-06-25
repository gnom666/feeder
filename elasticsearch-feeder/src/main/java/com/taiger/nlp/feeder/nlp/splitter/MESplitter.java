package com.taiger.nlp.feeder.nlp.splitter;

import java.io.IOException;

import org.springframework.util.Assert;

import com.taiger.nlp.feeder.model.Constants;
import com.taiger.nlp.feeder.model.Text;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

@Log4j2
@Getter
@Setter
public class MESplitter implements SentenceSplitter {

	private SentenceDetectorME detector;
	
	public MESplitter() {
		initialize();
	}
	
	public MESplitter initialize () {
		try {
			SentenceModel model = new SentenceModel(MESplitter.class.getClassLoader().getResourceAsStream(Constants.SENTENCE_EN));
			this.detector = new SentenceDetectorME(model);
		} 	catch (IOException e) {
			log.error(e.getMessage());
		}
		
		return this;
	}
	
	@Override
	public Text detect(String text) {
		Assert.hasText(text, "text shouldn't be null or empty");
		Text result = new Text();
		String[] sentences = detector.sentDetect(text);
		for (String sentence : sentences) {
			result.getSentences().add(sentence);
		}
		return result;
	}

}
