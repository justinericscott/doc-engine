package com.github.justinericscott.docengine.service.content;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.justinericscott.docengine.models.Instance.DocumentInstance;
import com.github.justinericscott.docengine.pipeline.Project;

public class InstantiationServiceImpl implements InstantiationService {
	
	@Autowired
	private ContentService _content;
	@Autowired
	private InstanceService _instance;

	public InstantiationServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public DocumentInstance instantiate(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

}
