package org.ileler.jenkins.plugin.sbs;

import hudson.Extension;
import hudson.model.RootAction;
import jenkins.model.ModelObjectWithContextMenu;
import org.ileler.jenkins.plugin.sbs.dao.EnvDAO;
import org.ileler.jenkins.plugin.sbs.dao.ProfileDAO;
import org.ileler.jenkins.plugin.sbs.dao.ServerDAO;
import org.ileler.jenkins.plugin.sbs.model.Env;
import org.ileler.jenkins.plugin.sbs.model.Profile;
import org.ileler.jenkins.plugin.sbs.model.Server;
import org.ileler.jenkins.plugin.sbs.util.JsonDB;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.util.List;

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

    private Controller controller = new Controller();

    public Controller getController() {
        return controller;
    }

}
