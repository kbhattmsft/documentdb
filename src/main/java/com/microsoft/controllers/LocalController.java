package com.microsoft.controllers;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.config.LocalConfig;
import com.microsoft.data.access.DocDBDAO;
import com.microsoft.model.SampleModel;

@RestController
@Profile("local")
public class LocalController {

	@Autowired
	private ApplicationContext ctx;

	@RequestMapping("/local")
	public String getEnv() throws IOException {
		LocalConfig config = ctx.getBean(LocalConfig.class);
		return new Gson().toJson(config.getDocumentDBProps());

	}

	@RequestMapping("/save")
	public String save() throws BeansException, JSONException, IOException, DocumentClientException {
		SampleModel data = new SampleModel();
		data.setId(UUID.randomUUID().toString());
		data.setData("Hello World!");
		return ctx.getBean("docDbDao", DocDBDAO.class).save(data);
	}
}
