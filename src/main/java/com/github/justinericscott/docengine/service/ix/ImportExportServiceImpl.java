package com.github.justinericscott.docengine.service.ix;

import static com.github.justinericscott.docengine.util.Utils.create;
import static com.github.justinericscott.docengine.util.Utils.get;
import static com.github.justinericscott.docengine.util.Utils.isNotNullAndExists;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.TokenDefinition;
import com.github.justinericscott.docengine.service.content.ContentService;
import com.github.justinericscott.docengine.service.token.TokenDictionaryService;

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
	public final File exportToFile(final Class<?> type, final String path) {
		if (isNotNullOrEmpty(type)) {
			Collection<?> objects = null;
			if (type.equals(Content.class)) {
				objects = Arrays.asList(_contents.findAll().getContents());
			} else if (type.equals(TokenDefinition.class)) {
				objects = Arrays.asList(_dictionary.findAll().getTokens());
			}
			if (isNotNullOrEmpty(objects)) {
				final File file = create(path, true);
				if (isNotNullAndExists(file)) {
					return _writer.write(type, file, objects, true);
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
	public Iterable<?> importFromFile(final Class<?> type, final String path) {
		if (isNotNullOrEmpty(type)) {
			final Iterable<?> objects = _reader.read(type, get(path));
			if (isNotNullOrEmpty(objects)) {
				final Iterable<?> contents = _contents.save(objects);
				return contents;
			} else {
				LOG.warn("No objects where created from the provided Class and File path!");
			}
		} else {
			LOG.warn("The class must not be null!");
		}
		return null;
	}

	@Override
	public Iterable<? extends Content> importFromFile(final Class<? extends Content>[] types, final String path) {
		if (isNotNullOrEmpty(types)) {
			final Iterable<? extends Content> iter = _reader.read(types, get(path));
			if (isNotNullOrEmpty(iter)) {
				final Iterable<? extends Content> saved = _contents.save(iter, null);
				return saved;
			} else {
				LOG.warn("No objects where created from the provided Class and File path!");
			}
		} else {
			LOG.warn("The class must not be null!");
		}
		return null;
	}
}