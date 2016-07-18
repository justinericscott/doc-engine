package com.itgfirm.docengine.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.builder.model.KieSessionModel.KieSessionType;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.definition.KiePackage;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.util.Utils;

/**
 * 
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
class DroolsExpertLogicServiceImpl implements LogicService {
	private static final Logger LOG = LogManager.getLogger(DroolsExpertLogicServiceImpl.class);
	private static final String DEFAULT_GLOBAL = "response";
	private static final String DEFAULT_KIE_BASE_NAME = "KieBase";
	private static final String DEFAULT_KIE_SESSION_NAME = "KieSession";
	private static final ClockTypeOption DEFAULT_CLOCK = ClockTypeOption.get("realtime");
	private static final KieFileSystem FS = KieServices.Factory.get().newKieFileSystem();
	private static final KieServices SERVICES = KieServices.Factory.get();
	private static KieContainer kie = init();

	public void addResource(File file) {
		FS.write(ResourceFactory.newFileResource(file));
		SERVICES.newKieBuilder(FS).buildAll();
		kie = SERVICES.newKieContainer(SERVICES.getRepository().getDefaultReleaseId());
	}

	public List<String> process(Object tokens) {
		if (Utils.isNotNullOrEmpty(tokens)) {
			List<KiePackage> pkgs = (List<KiePackage>) kie.getKieBase().getKiePackages();
			if (Utils.isNotNullOrEmpty(pkgs)) {
				LOG.debug(kie.getKieBase().getKiePackages().iterator().next().getName());
				ArrayList<String> response = new ArrayList<String>();
				Object[] assets = {
						tokens, response
				};
				StatelessKieSession session = getSession();
				session.setGlobal(DEFAULT_GLOBAL, response);
				session.execute(Arrays.asList(assets));
				return response;
			} else {
				LOG.warn("No KIE Packages Found In KIE Base!");
			}
		} else {
			LOG.warn("Tokens Cannot Be Null Or Empty!");
		}
		return null;
	}

	private static KieBaseModel createKieBaseModel(KieModuleModel module) {
		KieBaseModel base = module.newKieBaseModel(DEFAULT_KIE_BASE_NAME);
		base.setDefault(true);
		base.setEqualsBehavior(EqualityBehaviorOption.EQUALITY);
		base.setEventProcessingMode(EventProcessingOption.CLOUD);
		return base;
	}

	private static KieSessionModel createKieSessionModel(KieBaseModel base) {
		KieSessionModel session = base.newKieSessionModel(DEFAULT_KIE_SESSION_NAME);
		session.setDefault(true);
		session.setType(KieSessionType.STATELESS);
		session.setClockType(DEFAULT_CLOCK);
		return session;
	}

	private StatelessKieSession getSession() {
		return kie.newStatelessKieSession();
	}

	private static KieContainer init() {
		KieModuleModel module = SERVICES.newKieModuleModel();
		KieBaseModel base = createKieBaseModel(module);
		createKieSessionModel(base);
		SERVICES.newKieBuilder(FS).buildAll();
		return SERVICES.newKieContainer(SERVICES.getRepository().getDefaultReleaseId());
	}
}