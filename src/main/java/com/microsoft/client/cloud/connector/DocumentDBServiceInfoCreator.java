package com.microsoft.client.cloud.connector;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

public class DocumentDBServiceInfoCreator extends CloudFoundryServiceInfoCreator<DocumentDBServiceInfo> {

	public static final String DOCUMENTDB_PREFIX = "documentdb";

	public DocumentDBServiceInfoCreator() {
		super(new Tags(), DOCUMENTDB_PREFIX);
	}

	@Override
	public DocumentDBServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		String id = (String) serviceData.get("name");

		Map<String, Object> credentials = getCredentials(serviceData);
		String uri = getUriFromCredentials(credentials);
		System.out.println("uri: " + uri);
		return new DocumentDBServiceInfo(id, uri);
	}

}
