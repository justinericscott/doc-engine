package com.itgfirm.docengine.service;

import java.util.Map;

import com.itgfirm.docengine.types.ExternalSchemaImpl;

public interface BusinessDataService {
	
	void execute(String sql);
	
	void execute(String[] script);
	
	ExternalSchemaImpl getExternalSchema();

	Map<String, Object> queryForMap(String sql);
	
	int update(String sql);
	
	Integer[] update(String[] script);
}
