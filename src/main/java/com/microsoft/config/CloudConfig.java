package com.microsoft.config;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.google.gson.Gson;

@Configuration
@Profile("cloud")
public class CloudConfig implements Config {

	@Bean
	public DocumentDBProps getDocumentDBProps() throws JSONException, IOException {
		JSONObject env = new JSONObject(System.getenv("VCAP_SERVICES"));
		JSONObject creds = env.getJSONArray("documentdb").getJSONObject(0).getJSONObject("credentials");
		DocumentDBProps props = new Gson().fromJson(creds.toString(), DocumentDBProps.class);
		return props;
	}

}
