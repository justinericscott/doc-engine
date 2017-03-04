package com.github.justinericscott.docengine.service.token;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.justinericscott.docengine.repository.token.BusinessDataRepository;
import com.github.justinericscott.docengine.service.token.types.ExternalSchema;

@Service
@Transactional(AUTOWIRE_QUALIFIER_JDBC_TX)
final class BusinessDataServiceImpl implements BusinessDataService {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessDataServiceImpl.class);

	@Autowired
	private BusinessDataRepository _business;
	
	BusinessDataServiceImpl() {
		LOG.debug("Creating new Business Data Service.");
	}

	@Override
	public final void execute(final String sql) {
		_business.execute(sql);
	}

	@Override
	public final void execute(final String[] script) {
		_business.execute(script);
	}

	@Override
	public final ExternalSchema getExternalSchema() {
		return _business.getExternalSchema();
	}

	@Override
	public final Map<String, Object> queryForMap(final String sql) {
		return _business.queryForMap(sql);
	}
	
	@Override
	public final int update(final String sql) {
		return _business.update(sql);
	}
	
	@Override
	public final Integer[] update(final String[] script) {
		return _business.update(script);
	}
}