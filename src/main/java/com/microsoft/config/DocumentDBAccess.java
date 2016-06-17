package com.microsoft.config;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.azure.documentdb.DocumentClientException;
import com.microsoft.azure.documentdb.DocumentCollection;
import com.microsoft.azure.documentdb.RequestOptions;
import com.microsoft.client.cloud.connector.DocumentClientFactory;

@Service("documentDBAccess")
public class DocumentDBAccess {

	@Autowired
	private ApplicationContext ctx;

	public DocumentCollection getDocumentDBCollection(String dabaseLink, String collectionId)
			throws BeansException, JSONException, IOException {

		DocumentClient client = ctx.getBean(DocumentClientFactory.class).getDocumentClient();
		List<DocumentCollection> collectionList = client
				.queryCollections(dabaseLink, "SELECT * FROM root r WHERE r.id='" + collectionId + "'", null)
				.getQueryIterable().toList();

		return collectionList.size() > 0 ? collectionList.get(0) : null;
	}

	public Database getDocumentDB(String databaseId) throws BeansException, JSONException, IOException {
		DocumentClient client = ctx.getBean(DocumentClientFactory.class).getDocumentClient();
		List<Database> databases = client.queryDatabases("SELECT * FROM root r WHERE r.id='" + databaseId + "'", null)
				.getQueryIterable().toList();
		return databases.size() > 0 ? databases.get(0) : null;
	}

	public DocumentCollection createDocumentCollection(String databaseLink, String collectionId)
			throws DocumentClientException, BeansException, JSONException, IOException {
		DocumentCollection collectionDefinition = new DocumentCollection();
		collectionDefinition.setId(collectionId);
		RequestOptions requestOptions = new RequestOptions();
		requestOptions.setOfferThroughput(1000);
		DocumentClient client = ctx.getBean(DocumentClientFactory.class).getDocumentClient();

		DocumentCollection collection = client.createCollection(databaseLink, collectionDefinition, requestOptions)
				.getResource();
		return collection;
	}
}
