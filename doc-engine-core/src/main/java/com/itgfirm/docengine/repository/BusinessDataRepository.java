package com.itgfirm.docengine.repository;

import java.util.List;
import java.util.Map;

import com.itgfirm.docengine.types.ExternalSchemaImpl;

public interface BusinessDataRepository {

	void execute(String sql);
	
	void execute(String[] script);
	
	List<Map<String, Object>> queryForList(String sql);
	
	Map<String, Object> queryForMap(String sql);
	
	int update(String sql);
	
	Integer[] update(String[] script);
	
	ExternalSchemaImpl getExternalSchema();
}
