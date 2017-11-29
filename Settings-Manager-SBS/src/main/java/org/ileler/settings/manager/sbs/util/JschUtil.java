package org.ileler.settings.manager.sbs.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月27日 上午20:11
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class JschUtil {

    private static class MyUserInfo implements UserInfo {
        @Override
        public String getPassphrase() {
            System.out.println("getPassphrase");
            return null;
        }

        @Override
        public String getPassword() {
            System.out.println("getPassword");
            return null;
        }

        @Override
        public boolean promptPassword(String s) {
            System.out.println("promptPassword:" + s);
            return false;
        }

        @Override
        public boolean promptPassphrase(String s) {
            System.out.println("promptPassphrase:" + s);
            return false;
        }

        @Override
        public boolean promptYesNo(String s) {
            System.out.println("promptYesNo:" + s);
            return true;// notice here!
        }

        @Override
        public void showMessage(String s) {
            System.out.println("showMessage:" + s);
        }
    }

    public static void main(String[] args) {
        System.out.println(connect("scp://172.16.2.21:22/home/mpr/ms", "mpr", "Mprwebsite2008"));
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println(exec("scp://172.16.2.21:8585/home/mpr/ms", "mpr", "Mprwebsite2007", "ls -l"));
    }

    private static Session openConnectionInternal(String url, String username,
                                   String password) throws JSchException {
        Matcher matcher = Pattern.compile("(?:scp://)?(\\d+.*?)(?::(\\d+.*?))?/.*").matcher(url);
        int count = matcher.find() ? matcher.groupCount() : -1;
        String host = count > 0 ? matcher.group(1) : null;
        String port = count > 1 ? matcher.group(2) : null;
        if (StringUtils.isEmpty(host))  return null;

        JSch jsch = new JSch();
        Properties config = new Properties();
        config.setProperty( "StrictHostKeyChecking", "no" );
        Session session = jsch.getSession(username, host, StringUtils.isEmpty(port) ? 22 : Integer.valueOf(port));
        session.setConfig(config);
        session.setPassword(password);
        session.setUserInfo(new MyUserInfo());
        session.connect();
        return session;
    }

    public static String connect(String url, String username,
            String password) {
        try {
            Session session = openConnectionInternal(url, username, password);

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand("pwd");
            ((ChannelExec) channel).setErrStream(System.err);
            channel.setInputStream(null);

            InputStream in = channel.getInputStream();

            channel.connect();

            int exitCode = exitCode(in, channel);

            channel.disconnect();
            session.disconnect();
            return exitCode == 0 ? "success" : "exit:" + exitCode;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String exec(String url, String username,
                                  String password, String command) {
        try {
            Session session = openConnectionInternal(url, username, password);

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            ((ChannelExec) channel).setErrStream(System.err);
            channel.setInputStream(null);

            InputStream in = channel.getInputStream();

            channel.connect();

            int exitCode = exitCode(in, channel);

            channel.disconnect();
            session.disconnect();
            return exitCode == 0 ? "success" : "exit:" + exitCode;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    private static int exitCode(InputStream in, Channel channel) {
        try {
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0)
                        break;
                    System.out.print(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    if (in.available() > 0)
                        continue;
                    return channel.getExitStatus();
                }
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return -1;
    }

}
