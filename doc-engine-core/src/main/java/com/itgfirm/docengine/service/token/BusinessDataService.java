package com.itgfirm.docengine.service.token;

import java.util.Map;

import com.itgfirm.docengine.service.token.types.ExternalSchema;

public interface BusinessDataService {
	
	void execute(String sql);
	
	void execute(String[] script);
	
	ExternalSchema getExternalSchema();

	Map<String, Object> queryForMap(String sql);
	
	int update(String sql);
	
	Integer[] update(String[] script);
}
