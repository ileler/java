/**
 * @(#)MainDialog.java 1.0 2016年9月9日
 * @Copyright:  Copyright 2007 - 2016 MPR Tech. Co. Ltd. All Rights Reserved.
 * @Description: 
 * 
 * Modification History:
 * Date:        2016年9月9日
 * Author:      zhangle
 * Version:     1.0.0.0
 * Description: (Initialize)
 * Reviewer:    
 * Review Date: 
 */
package com.ileler.arcutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ArcUtil {
    
    private static final String OUTPUT_FILE_NAME = ".arcutil_" + System.currentTimeMillis() + ".bat";
    private static final String OUTPUT_FILE_PATH = System.getProperty("java.io.tmpdir") + File.separator + OUTPUT_FILE_NAME;
    
    private String arcConfigPath = null;
    private FileOutputStream outputStream = null;
    
    public ArcUtil(String arcConfigPath, List<String> commitPaths) {
        
        this.arcConfigPath = arcConfigPath;
        
        try {
            outputStream = new FileOutputStream(OUTPUT_FILE_PATH);
            
            sendInfo("@chcp 65001", false, false);
//            sendInfo("exec: " + OUTPUT_FILE_NAME);
            
            boolean isExec = false;
            
            if (arcConfigPath == null) {
                sendInfo("[.arcconfig]: null");
            } else if (!new File(arcConfigPath).exists() || !new File(arcConfigPath).isFile()) {
                sendInfo("[.arcconfig]: " + arcConfigPath + " -invalid");
            } 
            
            String paths = "";
            if (commitPaths == null || commitPaths.size() < 1) {
                sendInfo("[commit files]: null");
            } else {
                sendInfo("[commit files]: ");
                for (String commitPath : commitPaths) {
                    if (commitPath == null || "".equals(commitPath.trim()))     continue;
                    boolean invalid = false;
//                    || (new File(commitPath).exists() && new File(commitPath).isDirectory())
                    if (!commitPath.startsWith(arcConfigPath)) {
                        invalid = true;
                    }
                    if (!invalid) {
                        paths += (" \"" + commitPath.replace(this.arcConfigPath, ".") + "\"");
                    }
                    sendInfo("\t" + commitPath + (invalid ? " -invalid" : ""));
                }
                if (!"".equals(paths)) {
                    isExec = true;
                }
            }
            
            if (isExec) {
                sendInfo("PATH:");
                sendInfo("\t%PATH%");
                sendInfo("arc diff --create" + paths, false, false);
            }
            exit();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public String exec() {
        boolean status = false;
        try {
            Process process = Runtime.getRuntime().exec("cmd /c start /wait " + OUTPUT_FILE_PATH, null, new File(arcConfigPath));
            status = process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            new File(OUTPUT_FILE_PATH).delete();
        }
        return status ? "S" : "F";
    }
    
    private void exit() throws IOException {
        sendInfo("done.", true);
    }
    
    private void sendInfo(String info) throws IOException {
        sendInfo(info, false);
    }
    
    private void sendInfo(String info, boolean isClose) throws IOException {
        sendInfo(info, true, isClose);
    }
    
    private void sendInfo(String info, boolean isMsg, boolean isClose) throws IOException {
        if (isMsg) {
            outputStream.write(("@echo " + info + "\n").getBytes());
        } else {
            outputStream.write((" " + info + "\n").getBytes());
        }
        if (isClose) {
            outputStream.close();
        }
    }
    
    public static void main(String[] args) {
        if (args != null && args.length > 1) {
            System.out.println(new ArcUtil(args[0], Arrays.asList(Arrays.copyOfRange(args, 1, args.length))).exec());
        } else {
            System.out.println("args is invalid.");
        }
    }

}

