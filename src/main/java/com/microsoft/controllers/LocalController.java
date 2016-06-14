package com.microsoft.controllers;

import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("local")
public class LocalController {

	@Autowired
	private ApplicationContext ctx;

	@RequestMapping("/local")
	public String getEnv() throws IOException, ParseException {
		Resource resource = ctx.getResource("classpath:env.json");
		JSONParser parser = new JSONParser();

		JSONObject obj = (JSONObject) parser.parse(new InputStreamReader(resource.getInputStream()));
		return obj.keySet().toString();

	}
}
