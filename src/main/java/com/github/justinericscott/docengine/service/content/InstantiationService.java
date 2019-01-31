package com.github.justinericscott.docengine.service.content;

import com.github.justinericscott.docengine.models.Instance.DocumentInstance;
import com.github.justinericscott.docengine.pipeline.Project;

public interface InstantiationService {

	DocumentInstance instantiate(Project project);
}
