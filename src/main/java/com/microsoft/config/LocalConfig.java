package com.microsoft.config;

import java.io.IOException;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import com.google.common.io.Files;
import com.google.gson.Gson;

@Configuration
@Profile("local")
public class LocalConfig {

	@Autowired
	private ApplicationContext ctx;

	@Bean
	public DocumentDBProps getDocumentDBProps() throws JSONException, IOException {
		Resource resource = ctx.getResource("classpath:env.json");
		JSONObject env = new JSONObject(Files.toString(resource.getFile(), Charset.defaultCharset()));
		JSONObject creds = env.getJSONObject("system_env_json").getJSONObject("VCAP_SERVICES")
				.getJSONArray("documentdb").getJSONObject(0).getJSONObject("credentials");
		DocumentDBProps props = new Gson().fromJson(creds.toString(), DocumentDBProps.class);
		return props;
	}

}
