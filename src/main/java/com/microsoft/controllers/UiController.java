package com.microsoft.controllers;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.microsoft.config.CloudConfig;

@RestController
@Profile("cloud")
public class UiController {

	@Autowired
	private ApplicationContext ctx;

	@RequestMapping("/env")
	public String getDocumentDB() {
		JSONObject vcap = new JSONObject(System.getenv("VCAP_SERVICES"));
		JSONObject creds = vcap.getJSONArray("documentdb").getJSONObject(0).getJSONObject("credentials");
		return creds.toString();
	}

	@RequestMapping("/vcap")
	public String getEnv() throws IOException {
		// Try to parse VCAP_SERVICES
		JSONObject vcap = new JSONObject(System.getenv("VCAP_SERVICES"));
		return new Gson().toJson(vcap);
	}

	@RequestMapping("/cloud")
	public String getCloud() {
		CloudConfig cloudConfig = ctx.getBean(CloudConfig.class);
		return new Gson().toJson(cloudConfig.cloud().getCloudProperties());
	}

	@RequestMapping("/service")
	public String getService() {
		CloudConfig cloudConfig = ctx.getBean(CloudConfig.class);
		return new Gson().toJson(cloudConfig.cloud().getServiceInfo("docdb-dev"));
	}

}
