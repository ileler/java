import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Copyright:   Copyright 2007 - 2017 MPR Tech. Co. Ltd. All Rights Reserved.
 * Date:        2017年11月02日 上午16:37
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: Initialize
 */
public class Test {

    public static void main(String[] args) throws Exception {
        exec1("ls -l");
        System.out.println("------------------------------------------------------------------------------------------");
        exec1("ls xx");
    }

    private static void exec1(String commands) throws Exception {
        System.out.println(commands);
        ProcessBuilder builder = new ProcessBuilder(commands.split(" "));
        builder.redirectErrorStream(true);
        final Process process = builder.start();
        int i = process.waitFor();
        System.out.println("exit code:" + i);
        BufferedReader stdBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String stdOut = null;
        while ((stdOut = stdBufferedReader.readLine()) != null) {
            System.out.println("out:" + stdOut);
        }
    }

    private static void exec2(String commands) throws Exception {
        System.out.println(commands);
        Runtime runtime = Runtime.getRuntime();
        final Process exec = runtime.exec(commands);
        int i = exec.waitFor();
        System.out.println("exit code:" + i);
        new Thread(new Runnable() {
            public void run() {
                try {
                    BufferedReader errBufferedReader = new BufferedReader(new InputStreamReader(exec.getErrorStream()));
                    String errOut = null;
                    while ((errOut = errBufferedReader.readLine()) != null) {
                        System.out.println("err:" + errOut);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                try {
                    BufferedReader stdBufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
                    String stdOut = null;
                    while ((stdOut = stdBufferedReader.readLine()) != null) {
                        System.out.println("std:" + stdOut);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
