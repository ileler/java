package org.ileler.jenkins.plugin.sbs;

import hudson.Extension;
import hudson.model.RootAction;
import org.ileler.jenkins.plugin.sbs.controller.EnvController;
import org.ileler.jenkins.plugin.sbs.controller.ProfileController;
import org.ileler.jenkins.plugin.sbs.controller.ServerController;
import org.ileler.jenkins.plugin.sbs.dao.EnvDAO;
import org.ileler.jenkins.plugin.sbs.dao.ProfileDAO;
import org.ileler.jenkins.plugin.sbs.dao.ServerDAO;

/**
 * @author kerwin612
 */
@Extension
public class Root implements RootAction {

    public String getIconFileName() {
        return "gear.png";
    }

    public String getDisplayName() {
        return "SBS Settings";
    }

    public String getUrlName() {
        return "sbs-settings";
    }

    private EnvDAO envDAO = new EnvDAO();

    private ServerDAO serverDAO = new ServerDAO(envDAO);

    private ProfileDAO profileDAO = new ProfileDAO(envDAO, serverDAO);

    private EnvController envController = new EnvController(envDAO);

    private ServerController serverController = new ServerController(serverDAO);

    private ProfileController profileController = new ProfileController(profileDAO);

    public EnvController getEnvController() {
        return envController;
    }

    public ServerController getServerController() {
        return serverController;
    }

    public ProfileController getProfileController() {
        return profileController;
    }

}
