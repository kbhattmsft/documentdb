package com.microsoft.client.cloud.connector;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.Database;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.config.Config;
import com.microsoft.config.DocumentDBProps;

@Service
public class DocumentDBFactory {

	@Autowired
	private ApplicationContext ctx;

	@Bean
	public DocumentClient getDocumentClient() throws BeansException, JSONException, IOException {
		DocumentDBProps props = ctx.getBean(Config.class).getDocumentDBProps();
		DocumentClient docDbClient = new DocumentClient(props.getDocumentdb_host(), props.getDocumentdb_key(),
				ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);

		return docDbClient;
	}

	@Bean
	public Database getDocumentDB() throws BeansException, JSONException, IOException {
		DocumentDBProps props = ctx.getBean(Config.class).getDocumentDBProps();
		DocumentClient client = ctx.getBean(DocumentDBFactory.class).getDocumentClient();
		List<Database> databases = client
				.queryDatabases("SELECT * FROM root r WHERE r.id='" + props.getDocumentdb_database() + "'", null)
				.getQueryIterable().toList();
		return databases.size() > 0 ? databases.get(0) : null;
	}
}
