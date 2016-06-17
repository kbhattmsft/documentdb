package com.microsoft.data.access;

import java.io.IOException;
import java.util.UUID;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.Document;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.ResourceResponse;
import com.microsoft.client.cloud.connector.DocumentClientFactory;
import com.microsoft.config.DocumentDBAccess;
import com.microsoft.config.DocumentDBProps;
import com.microsoft.config.LocalConfig;
import com.microsoft.model.SampleModel;

@Service("docDbDao")
public class DocDBDAO {

	@Autowired
	private ApplicationContext ctx;

	public String save(SampleModel data) throws JSONException, IOException, DocumentClientException {
		LocalConfig config = ctx.getBean(LocalConfig.class);
		DocumentDBAccess dao = ctx.getBean(DocumentDBAccess.class);
		DocumentDBProps props = config.getDocumentDBProps();

		Database docDB = dao.getDocumentDB(props.getDocumentdb_database());

		DocumentClient client = ctx.getBean(DocumentClientFactory.class).getDocumentClient();
		Document docDbDoc = new Document(new Gson().toJson(data));
		DocumentCollection collection = dao.createDocumentCollection(docDB.getSelfLink(), UUID.randomUUID().toString());
		ResourceResponse<Document> doc = client.createDocument(collection.getSelfLink(), docDbDoc, null, true);
		return String.valueOf(doc.getStatusCode());
	}

}
