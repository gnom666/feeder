package com.taiger.nlp.feeder.services;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/")
public class GeneralServices {
	
	@RequestMapping(value="/echo", method=RequestMethod.GET, produces="application/text;charset=UTF-8")
    public String echo(@RequestParam(value="str", defaultValue="") String str) {
		Assert.notNull(str, "sentence shouldn't be null");
		log.info("ECHO : " + str);
		return str;
    }
	
}
