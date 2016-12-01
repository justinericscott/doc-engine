package com.itgfirm.docengine.service;

import static com.itgfirm.docengine.util.Constants.*;
import static com.itgfirm.docengine.util.Utils.*;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.repository.ContentRepository;
import com.itgfirm.docengine.repository.TokenDictionaryRepository;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
public class ImportExportServiceImpl implements ImportExportService {
	private static final Logger LOG = LoggerFactory.getLogger(ImportExportServiceImpl.class);

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
	private ContentRepository content;

	@Autowired
	private TokenDictionaryRepository dictionary;

	@Autowired
	private ExcelReaderService reader;

	@Autowired
	private ExcelWriterService writer;

	@Override
	public final File exportToFile(final Class<?> clazz, final String path) {
		if (isNotNullOrEmpty(clazz)) {
			Iterable<?> objects = null;
			if (clazz.equals(ContentJpaImpl.class)) {
				objects = content.findAll();
			} else if (clazz.equals(TokenDefinitionJpaImpl.class)) {
				objects = dictionary.findAll();
			}
			if (isNotNullOrEmpty(objects)) {
				final File file = create(path, false);
				if (isNotNullAndExists(file)) {
					return writer.write(clazz, file, objects, true);
				} else {
					LOG.debug("The for the provided path does not exist!");
				}
			}
		} else {
			LOG.debug("The class must not be null!");
		}

		return null;
	}

	@Override
	public Iterable<?> importFromFile(final Class<?> clazz, final String path) {
		if (isNotNullOrEmpty(clazz)) {
			final Iterable<?> objects = reader.read(clazz, get(path));
			if (isNotNullOrEmpty(objects)) {
				return objects;
			} else {
				LOG.debug("No objects where created from the provided Class and File path!");
			}
		} else {
			LOG.debug("The class must not be null!");
		}
		return null;
	}
}