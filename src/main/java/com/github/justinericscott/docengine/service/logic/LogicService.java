package com.github.justinericscott.docengine.service.logic;

import java.io.File;

public interface LogicService {
	
	void load(File file);
	
	Iterable<String> stateless(Object obj);
}