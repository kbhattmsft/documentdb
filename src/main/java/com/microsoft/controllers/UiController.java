package com.microsoft.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.config.Config;
import com.microsoft.data.access.DocDBDAO;
import com.microsoft.model.SampleModel;

@RestController
public class UiController {

	@Autowired
	private ApplicationContext ctx;

	@RequestMapping("/props")
	public String getProps() throws BeansException, JSONException, IOException {
		Gson gson = new Gson();

		return gson.toJson(ctx.getBean(Config.class).getDocumentDBProps());
	}

	@RequestMapping("/save")
	public String save() throws BeansException, JSONException, IOException, DocumentClientException {
		SampleModel data = new SampleModel();
		data.setCollectionId("Telemetry");
		data.setId(UUID.randomUUID().toString());
		data.setData(String.valueOf(System.currentTimeMillis()));
		data.setType("timestamp");
		return ctx.getBean("docDbDao", DocDBDAO.class).save(data);
	}

	@RequestMapping("/loadAll")
	public String loadCollections() throws IOException {
		DocDBDAO dao = ctx.getBean(DocDBDAO.class);
		List<DocumentCollection> collections = dao
				.loadAllCollectionsByDatabase(ctx.getBean(Config.class).getDocumentDBProps().getDocumentdb_database());
		Gson gson = new Gson();
		List<SampleModel> data = new ArrayList<SampleModel>();
		for (DocumentCollection collection : collections) {
			List<Document> docs = dao.loadDocumentsByCollection(collection.getSelfLink());
			for (Document doc : docs) {
				data.add(gson.fromJson(doc.toString(), SampleModel.class));
			}
		}
		return new Gson().toJson(data);
	}
}
