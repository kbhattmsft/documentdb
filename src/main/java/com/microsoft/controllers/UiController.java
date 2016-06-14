package com.microsoft.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.microsoft.config.CloudConfig;
import com.microsoft.config.DocumentDBProps;

@RestController
public class UiController {

	@Autowired
	private ApplicationContext ctx;

	@RequestMapping("/test")
	public String testMessage() {
		return "Hello world";
	}

	@RequestMapping("/vcap")
	public String getEnv() {
		DocumentDBProps dbProps = new Gson().fromJson(System.getenv("credentials"), DocumentDBProps.class);

		return new Gson().toJson(dbProps);
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
