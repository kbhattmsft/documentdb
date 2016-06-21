package com.microsoft.config;

import java.io.IOException;

import org.json.JSONException;

public interface Config {

	public DocumentDBProps getDocumentDBProps() throws JSONException, IOException;
}
