package com.microsoft.controllers;

import org.json.JSONObject;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("cloud")
public class UiController {

	@RequestMapping("/env")
	public String getDocumentDB() {
		JSONObject vcap = new JSONObject(System.getenv("VCAP_SERVICES"));
		JSONObject creds = vcap.getJSONArray("documentdb").getJSONObject(0).getJSONObject("credentials");
		return creds.toString();
	}

}
