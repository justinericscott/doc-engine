package com.github.justinericscott.docengine.repository.token;

import java.util.List;
import java.util.Map;

import com.github.justinericscott.docengine.service.token.types.ExternalSchema;

public interface BusinessDataRepository {

	void execute(String sql);
	
	void execute(String[] script);
	
	List<Map<String, Object>> queryForList(String sql);
	
	Map<String, Object> queryForMap(String sql);
	
	int update(String sql);
	
	Integer[] update(String[] script);
	
	ExternalSchema getExternalSchema();
}
