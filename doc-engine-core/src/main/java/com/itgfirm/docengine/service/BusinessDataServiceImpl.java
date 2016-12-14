package com.itgfirm.docengine.service;

import static com.itgfirm.docengine.DocEngine.Constants.*;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itgfirm.docengine.repository.BusinessDataRepository;
import com.itgfirm.docengine.types.ExternalSchemaImpl;

@Service
@Transactional(transactionManager = AUTOWIRE_QUALIFIER_JDBC_TX)
public class BusinessDataServiceImpl implements BusinessDataService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BusinessDataServiceImpl.class);

	@Autowired
	private BusinessDataRepository repo;

	@Override
	public final void execute(final String sql) {
		repo.execute(sql);
	}

	@Override
	public final void execute(final String[] script) {
		repo.execute(script);
	}

	@Override
	public final ExternalSchemaImpl getExternalSchema() {
		return repo.getExternalSchema();
	}

	@Override
	public final Map<String, Object> queryForMap(final String sql) {
		return repo.queryForMap(sql);
	}

	
	@Override
	public final int update(final String sql) {
		return repo.update(sql);
	}

	
	@Override
	public final Integer[] update(final String[] script) {
		return repo.update(script);
	}
}