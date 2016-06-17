package com.microsoft.client.cloud.connector;

import java.io.IOException;

import org.json.JSONException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.microsoft.azure.documentdb.ConnectionPolicy;
import com.microsoft.azure.documentdb.ConsistencyLevel;
import com.microsoft.azure.documentdb.DocumentClient;
import com.microsoft.config.DocumentDBProps;
import com.microsoft.config.LocalConfig;

@Service
@Profile("local")
public class DocumentClientFactory {

	@Autowired
	private ApplicationContext ctx;

	@Bean
	public DocumentClient getDocumentClient() throws BeansException, JSONException, IOException {

		DocumentDBProps props = ctx.getBean(LocalConfig.class).getDocumentDBProps();
		DocumentClient docDbClient = new DocumentClient(props.getDocumentdb_host(), props.getDocumentdb_key(),
				ConnectionPolicy.GetDefault(), ConsistencyLevel.Session);

		return docDbClient;
	}
}
