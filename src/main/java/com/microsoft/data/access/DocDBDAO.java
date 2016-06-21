package com.microsoft.data.access;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.springframework.beans.BeansException;
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
import com.microsoft.config.Config;
import com.microsoft.config.DocumentDBAccess;
import com.microsoft.config.DocumentDBProps;
import com.microsoft.model.SampleModel;

@Service("docDbDao")
public class DocDBDAO {

	@Autowired
	private ApplicationContext ctx;

	public String save(SampleModel data) throws JSONException, IOException, DocumentClientException {
		Config config = ctx.getBean(Config.class);
		DocumentDBAccess dbAccess = ctx.getBean(DocumentDBAccess.class);
		DocumentClient client = ctx.getBean(DocumentClientFactory.class).getDocumentClient();

		DocumentDBProps props = config.getDocumentDBProps();

		Database docDB = dbAccess.getDocumentDB(props.getDocumentdb_database());

		Document docDbDoc = new Document(new Gson().toJson(data));
		docDbDoc.set("entityType", data.getType());

		DocumentCollection collection = dbAccess.createDocumentCollection(docDB.getSelfLink(),
				UUID.randomUUID().toString());
		ResourceResponse<Document> doc = client.createDocument(collection.getSelfLink(), docDbDoc, null, true);
		return String.valueOf(doc.getStatusCode());
	}

	public List<DocumentCollection> loadCollections(String databaseLink)
			throws BeansException, JSONException, IOException {
		DocumentClient client = ctx.getBean(DocumentClientFactory.class).getDocumentClient();

		// Retrieve the collections
		List<DocumentCollection> collections = client
				.queryCollections(ctx.getBean(DocumentDBAccess.class).getDocumentDB(databaseLink).getSelfLink(),
						"SELECT * FROM root", null)
				.getQueryIterable().toList();

		return collections;
	}

	public List<Document> loadDocuments(String collectionLink) throws BeansException, JSONException, IOException {
		DocumentClient client = ctx.getBean(DocumentClientFactory.class).getDocumentClient();
		// Retrieve the TodoItem documents
		List<Document> docs = client.queryDocuments(collectionLink, "SELECT * FROM root r", null).getQueryIterable()
				.toList();
		return docs;
	}

}
