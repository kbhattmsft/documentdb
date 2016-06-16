package com.microsoft.controllers;

import java.io.IOException;
import java.nio.charset.Charset;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.io.Files;

@RestController
@Profile("local")
public class LocalController {

	@Autowired
	private ApplicationContext ctx;

	@RequestMapping("/local")
	public String getEnv() throws IOException {
		Resource resource = ctx.getResource("classpath:env.json");
		JSONObject env = new JSONObject(Files.toString(resource.getFile(), Charset.defaultCharset()));
		JSONObject creds = env.getJSONObject("system_env_json").getJSONObject("VCAP_SERVICES")
				.getJSONArray("documentdb").getJSONObject(0).getJSONObject("credentials");

		return creds.toString();

	}
}
