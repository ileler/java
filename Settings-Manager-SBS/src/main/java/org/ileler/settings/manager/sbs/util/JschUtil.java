package org.ileler.settings.manager.sbs.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;
import org.ileler.settings.manager.sbs.model.Server;
import org.ileler.settings.manager.sbs.model.Streams;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

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
        System.out.println(connect(new Server("test", "scp://172.16.2.21:22/home/mpr/ms", "mpr", "Mprwebsite2008", "")));
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.println(exec(new Server("test", "scp://172.16.2.21:8585/home/mpr/ms", "mpr", "Mprwebsite2007", ""), "ls -l"));
    }

    private static Session openConnectionInternal(Server server) throws JSchException {
        String username = server == null ? null : server.getUsername();
        String password = server == null ? null : server.getPassword();
        String host = server == null ? null : server.getHost();
        Integer port = server == null ? null : server.getPort();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password) || StringUtils.isEmpty(host)) return null;

        JSch jsch = new JSch();
        Properties config = new Properties();
        config.setProperty("StrictHostKeyChecking", "no");
        Session session = jsch.getSession(username, host, port == null ? 22 : port);
        session.setConfig(config);
        session.setPassword(password);
        session.setUserInfo(new MyUserInfo());
        session.connect();
        return session;
    }

    public static String connect(Server server) {
        try {
            Session session = openConnectionInternal(server);

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

    public static String exec(Server server, String command) {
        try {
            Session session = openConnectionInternal(server);

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
                }
                if (channel.isClosed()) {
                    if (in.available() > 0)
                        continue;
                    return channel.getExitStatus();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Streams executeCommand(Server server, String command) {
        ChannelExec channel = null;
        BufferedReader stdoutReader = null;
        BufferedReader stderrReader = null;
        Streams streams = null;
        try {
            Session session = openConnectionInternal(server);

            channel = (ChannelExec) session.openChannel("exec");

            channel.setCommand(command + "\n");

            stdoutReader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            stderrReader = new BufferedReader(new InputStreamReader(channel.getErrStream()));

            channel.connect();

            streams = CommandExecutorStreamProcessor.processStreams(stderrReader, stdoutReader);

            stdoutReader.close();
            stdoutReader = null;

            stderrReader.close();
            stderrReader = null;

            streams.setExitCode(channel.getExitStatus());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            IOUtil.close(stdoutReader);
            IOUtil.close(stderrReader);
            if (channel != null) {
                channel.disconnect();
            }
        }
        return streams;
    }

}
