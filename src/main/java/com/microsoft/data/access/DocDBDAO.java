package com.microsoft.data.access;

import java.io.IOException;
import java.util.List;

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
import com.microsoft.azure.documentdb.RequestOptions;
import com.microsoft.azure.documentdb.ResourceResponse;
import com.microsoft.client.cloud.connector.DocumentDBFactory;
import com.microsoft.model.SampleModel;

@Service("docDbDao")
public class DocDBDAO {

	@Autowired
	private ApplicationContext ctx;

	public String save(SampleModel data) throws JSONException, IOException, DocumentClientException {
		DocumentClient client = ctx.getBean(DocumentDBFactory.class).getDocumentClient();

		Database docDB = ctx.getBean(DocumentDBFactory.class).getDocumentDB();

		Document docDbDoc = new Document(new Gson().toJson(data));
		docDbDoc.set("entityType", data.getType());

		DocumentCollection collection = getCollection(docDB.getSelfLink(), data.getCollectionId());
		if (collection == null) {
			collection = createDocumentCollection(docDB.getSelfLink(), data.getCollectionId());
		}
		ResourceResponse<Document> doc = client.createDocument(collection.getSelfLink(), docDbDoc, null, true);
		return String.valueOf(doc.getStatusCode());
	}

	public List<DocumentCollection> loadAllCollectionsByDatabase(String databaseLink)
			throws BeansException, JSONException, IOException {
		DocumentClient client = ctx.getBean(DocumentDBFactory.class).getDocumentClient();

		// Retrieve the collections
		List<DocumentCollection> collections = client
				.queryCollections(ctx.getBean(DocumentDBFactory.class).getDocumentDB().getSelfLink(),
						"SELECT * FROM root", null)
				.getQueryIterable().toList();

		return collections;
	}

	public List<Document> loadDocumentsByCollection(String collectionLink) throws BeansException, JSONException, IOException {
		DocumentClient client = ctx.getBean(DocumentDBFactory.class).getDocumentClient();
		// Retrieve the documents
		List<Document> docs = client.queryDocuments(collectionLink, "SELECT * FROM root r", null).getQueryIterable()
				.toList();
		return docs;
	}

	public DocumentCollection createDocumentCollection(String databaseLink, String collectionId)
			throws DocumentClientException, BeansException, JSONException, IOException {
		DocumentCollection collectionDefinition = new DocumentCollection();
		collectionDefinition.setId(collectionId);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.setOfferThroughput(1000);
		DocumentClient client = ctx.getBean(DocumentDBFactory.class).getDocumentClient();

		DocumentCollection collection = client.createCollection(databaseLink, collectionDefinition, requestOptions)
				.getResource();
		return collection;
	}

	public DocumentCollection getCollection(String dabaseLink, String collectionId)
			throws BeansException, JSONException, IOException {

		DocumentClient client = ctx.getBean(DocumentDBFactory.class).getDocumentClient();
		List<DocumentCollection> collectionList = client
				.queryCollections(dabaseLink, "SELECT * FROM root r WHERE r.id='" + collectionId + "'", null)
				.getQueryIterable().toList();

		return collectionList.size() > 0 ? collectionList.get(0) : null;
	}

}
