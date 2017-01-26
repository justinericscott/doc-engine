package com.github.justinericscott.docengine.service.token;

import java.util.Map;

import com.github.justinericscott.docengine.service.token.types.ExternalSchema;

public interface BusinessDataService {
	
	void execute(String sql);
	
	void execute(String[] script);
	
	ExternalSchema getExternalSchema();

	Map<String, Object> queryForMap(String sql);
	
	int update(String sql);
	
	Integer[] update(String[] script);
}
