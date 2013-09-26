package mj.webapp.base.sysmanage;

import org.apache.log4j.Logger;

import mj.webapp.core.ActionExecute;
import mj.webapp.core.BaseAction;

public class Login extends BaseAction{
	
	private static Logger logger = Logger.getLogger(Login.class.getName());

	@Override
	public void beforeExecute() {
		logger.error("-->>beforeExecute");
	}

	@Override
	public void execute() {
		logger.error("-->>execute");
	}

	@Override
	public void afterExecute() {
		logger.error("-->>afterExecute");
	}

}
