package com.itgfirm.docengine.service.ix;

import static com.itgfirm.docengine.util.Utils.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.service.content.ContentService;
import com.itgfirm.docengine.service.token.TokenDictionaryService;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.types.Contents;
import com.itgfirm.docengine.types.TokenDefinitionJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
public class ImportExportServiceImpl implements ImportExportService {
	private static final Logger LOG = LoggerFactory.getLogger(ImportExportServiceImpl.class);

	@Autowired
	private ContentService _contents;

	@Autowired
	private TokenDictionaryService _dictionary;

	@Autowired
	private ExcelReaderService _reader;

	@Autowired
	private ExcelWriterService _writer;

	@Override
	public final File exportToFile(final Class<?> clazz, final String path) {
		if (isNotNullOrEmpty(clazz)) {
			Collection<?> objects = null;
			if (clazz.equals(ContentJpaImpl.class)) {
				objects = Arrays.asList(_contents.findAll().getContents());
			} else if (clazz.equals(TokenDefinitionJpaImpl.class)) {
				objects = Arrays.asList(_dictionary.findAll().getDefinitionsList());
			}
			if (isNotNullOrEmpty(objects)) {
				final File file = create(path, true);
				if (isNotNullAndExists(file)) {
					return _writer.write(clazz, file, objects, true);
				} else {
					LOG.warn("The for the provided path does not exist!");
				}
			}
		} else {
			LOG.warn("The class must not be null!");
		}

		return null;
	}

	@Override
	public <T> T importFromFile(final Class<T> clazz, final String path) {
		if (isNotNullOrEmpty(clazz)) {
			if (clazz.equals(Contents.class)) {
				final Collection<ContentJpaImpl> objects = (Collection<ContentJpaImpl>) _reader.read(ContentJpaImpl.class, get(path));	
				if (isNotNullOrEmpty(objects)) {
					Contents contents = new Contents(objects.toArray(new ContentJpaImpl[objects.size()]));
					return clazz.cast(_contents.save(contents));
				} else {
					LOG.warn("No objects where created from the provided Class and File path!");
				}
			}
		} else {
			LOG.warn("The class must not be null!");
		}
		return null;
	}
}