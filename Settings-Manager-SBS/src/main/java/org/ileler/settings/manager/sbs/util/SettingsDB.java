package org.ileler.settings.manager.sbs.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.ileler.settings.manager.sbs.model.Env;
import org.ileler.settings.manager.sbs.model.Profile;
import org.ileler.settings.manager.sbs.model.Server;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月25日 上午11:05
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class SettingsDB {

    private String filePath;
    private Document document;
    private Element profiles;
    private Element servers;
    private Map<String, Profile> profileMap;
    private Map<String, Server> serverMap;

    public SettingsDB(Env env) throws IOException, DocumentException {
        String path = env == null ? null : env.getPath();
        if (StringUtils.isEmpty(path)) {
            throw new NullPointerException("env invalid.");
        }
        File file = new File(path);
        if (!file.exists()) {
            FileOutputStream fileOutputStream = null;
            FileInputStream fileInputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                String template = env.getTemplate();
                if (StringUtils.isEmpty(template)) {
                    template = SettingsDB.class.getResource("/settings.xml").getFile();
                }
                File templateFile = new File(template);
                if (templateFile.exists() && templateFile.isFile()) {
                    fileInputStream = new FileInputStream(templateFile);
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while((length = fileInputStream.read(bytes)) > 0) {
                        fileOutputStream.write(bytes, 0, length);
                    }
                }
            } finally {
                if (fileInputStream != null) fileInputStream.close();
                if (fileOutputStream != null) fileOutputStream.close();
            }
        }

        document = new SAXReader().read(filePath = path);
        Element settings = document.getRootElement();
        if (settings == null || !"settings".equalsIgnoreCase(settings.getName())) {
            throw new IllegalArgumentException("template invalid.");
        }
        servers = settings.element("servers");
        if (servers == null) {
            servers = settings.addElement("servers");
        }
        loadServers();
        if (serverMap == null) serverMap = new Hashtable<>(0);
        profiles = settings.element("profiles");
        if (profiles == null) {
            profiles = settings.addElement("profiles");
        }
        loadProfiles();
        if (profileMap == null) profileMap = new Hashtable<>(0);
    }

    public List<Server> getServers() {
        return serverMap == null ? null : new ArrayList<>(serverMap.values());
    }

    public Server getServer(String id) {
        return serverMap.get(id);
    }

    public SettingsDB addServer(Server server) {
        if (server == null || StringUtils.isEmpty(server.getId())) return this;
        serverMap.put(server.getId(), server);
        return this;
    }

    public SettingsDB modServer(Server server) {
        return addServer(server);
    }

    public SettingsDB delServer(Object idOrServer) {
        serverMap.remove(idOrServer);
        return this;
    }

    private Server getServerFromElement(Element element) {
        if (element == null) return null;
        Element id = element.element("id");
        String idValue = id == null ? null : id.getStringValue();
        if (StringUtils.isEmpty(idValue)) return null;
        Element username = element.element("username");
        String usernameValue = username == null ? null : username.getStringValue();
        if (StringUtils.isEmpty(usernameValue)) return null;
        Element password = element.element("password");
        String passwordValue = password == null ? null : password.getStringValue();
        if (StringUtils.isEmpty(passwordValue)) return null;
        Element configuration = element.element("configuration");
        String urlValue = configuration.attributeValue("url");
        if (StringUtils.isEmpty(urlValue)) return null;
        String text = configuration.getText();
        return new Server(idValue, urlValue, usernameValue, passwordValue, text);
    }

    public List<Profile> getProfiles() {
        return profileMap == null ? null : new ArrayList<>(profileMap.values());
    }

    public Profile getProfile(String id) {
        return profileMap.get(id);
    }

    public SettingsDB addProfile(Profile profile) {
        if (profile == null || StringUtils.isEmpty(profile.getId())) return this;
        profileMap.put(profile.getId(), profile);
        return this;
    }

    public SettingsDB modProfile(Profile profile) {
        return addProfile(profile);
    }

    public SettingsDB delProfile(Object idOrProfile) {
        profileMap.remove(idOrProfile);
        return this;
    }

    private Profile getProfileFromElement(Element element) {
        if (element == null) return null;
        Element id = element.element("id");
        String idValue = id == null ? null : id.getStringValue();
        if (StringUtils.isEmpty(idValue)) return null;
        Element properties = element.element("properties");
        if (properties == null) return null;
        Element sid = properties.element("service.sid");
        String sidValue = sid == null ? null : sid.getStringValue();
        if (StringUtils.isEmpty(sidValue)) return null;
        Element port = properties.element("service.port");
        String portValue = port == null ? null : port.getStringValue();
        if (StringUtils.isEmpty(portValue)) return null;
        Element dPort = properties.element("service.dport");
        String dPortValue = dPort == null ? null : dPort.getStringValue();
//        if (StringUtils.isEmpty(dPortValue)) return null;
        Element dir = properties.element("service.dir");
        String dirValue = dir == null ? null : dir.getStringValue();
        if (StringUtils.isEmpty(dirValue)) return null;
        Element arg = properties.element("service.arg");
        String argValue = arg == null ? null : arg.getStringValue();
//        if (StringUtils.isEmpty(argValue)) return null;
        return new Profile(idValue, sidValue, dirValue, argValue, Integer.valueOf(portValue), dPortValue == null ? null : Integer.valueOf(dPortValue));
    }

    private void loadServers() {
        List<Element> list = servers == null ? null : servers.elements("server");
        if (list == null || list.size() < 1) return;
        serverMap = new Hashtable<>(0);
        for (Element server : list) {
            Server serverFromElement = getServerFromElement(server);
            if (serverFromElement == null) continue;
            serverMap.put(serverFromElement.getId(), serverFromElement);
            servers.remove(server);
        }
    }

    private void loadProfiles() {
        List<Element> list = profiles == null ? null : profiles.elements("profile");
        if (list == null || list.size() < 1) return;
        profileMap = new Hashtable<>(0);
        for (Element profile : list) {
            Profile profileFromElement = getProfileFromElement(profile);
            if (profileFromElement == null) continue;
            profileMap.put(profileFromElement.getId(), profileFromElement);
            profiles.remove(profile);
        }
    }

    public void save() {
        try {
            List<Server> servers = getServers();
            for (Server server : servers) {
                Element element = this.servers.addElement("server");
                element.addElement("id").setText(server.getId());
                element.addElement("username").setText(server.getUsername());
                element.addElement("password").setText(server.getPassword());
                Element configuration = element.addElement("configuration");
                configuration.addAttribute("url", server.getUrl());
                String configurationText = server.getConfiguration();
                if (!StringUtils.isEmpty(configurationText)) configuration.addText(configurationText);
                else configuration.addText("<strictHostKeyChecking>no</strictHostKeyChecking>");
            }
            List<Profile> profiles = getProfiles();
            for (Profile profile : profiles) {
                Element element = this.profiles.addElement("profile");
                element.addElement("id").setText(profile.getId());
                Element properties = element.addElement("properties");
                properties.addElement("service.sid").setText(profile.getSid());
                properties.addElement("service.dir").setText(profile.getDir());
                String argValue = profile.getArg();
                if (!StringUtils.isEmpty(argValue)) properties.addElement("service.arg").setText(argValue);
                properties.addElement("service.port").setText(profile.getPort().toString());
                Integer portValue = profile.getdPort();
                if (portValue != null) properties.addElement("service.dport").setText(profile.getdPort().toString());
            }
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(document.getXMLEncoding());
            XMLWriter writer = new XMLWriter(new FileOutputStream(filePath), format);
            writer.setEscapeText(false);
            writer.write(document);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
