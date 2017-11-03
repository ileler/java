package org.ileler.ideaplugin.deployres;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Plugin extends AnAction {

    private static final String CFG_NAME = "dr.cfg";
    private Map<String, StringBuffer> message;
    private List<String> cfgPaths;
    private File focusFile;
    private File cfgFile;

    @Override
    public void update(AnActionEvent event) {
        VirtualFile vFile = CommonDataKeys.VIRTUAL_FILE.getData(event.getDataContext());
        cfgFile = getCfgFile(event.getProject().getBaseDir(), vFile);
        focusFile = new File(vFile.getPath());
        event.getPresentation().setEnabled(cfgFile != null);
        event.getPresentation().setVisible(cfgFile != null);
    }

    @Contract("null -> false")
    private File getCfgFile(@Nullable VirtualFile rootDir, @Nullable VirtualFile focusFile) {
        if (rootDir == null || !rootDir.exists() || focusFile == null || !focusFile.exists())    return null;
        if (cfgPaths != null) {
            String _path = focusFile.getPath();
            for (String cfgPath : cfgPaths) {
                if (_path.startsWith(cfgPath)) return new File(cfgPath);
            }
        }
        if (focusFile.isDirectory()) {
            File cfgFile = new File(focusFile.getPath() + File.separator + CFG_NAME);
            if (cfgFile.exists()) {
                if (cfgPaths == null) {
                    cfgPaths = new ArrayList<>();
                }
                cfgPaths.add(cfgFile.getPath());
                return cfgFile;
            } else {
                if (focusFile.getPath().equals(rootDir.getPath()))  return null;
                return getCfgFile(rootDir, focusFile.getParent());
            }
        } else {
            return getCfgFile(rootDir, focusFile.getParent());
        }
    }

    private void copy(File src, File des) throws IOException {
        if (src == null || des == null)     return;
        if (src.isDirectory()) {
            File[] files = src.listFiles();
            des.mkdirs();
            for (File file : files) {
                copy(file, new File(des.getAbsolutePath() + File.separator + file.getName()));
            }
        } else {
            if (src.getName().equals(CFG_NAME)) return;
            des.getParentFile().mkdirs();
            try (InputStream is = new FileInputStream(src);
                 OutputStream os = new FileOutputStream(des);) {
                byte[] bs = new byte[2048];
                int len = -1;
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            } catch (Exception e) {
                throw e;
            }
        }
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        StringBuffer _message = new StringBuffer();
        String _path = focusFile.getPath();
        try {
            if (cfgFile == null) {
                _message.append(_path + "   -not found ["+CFG_NAME+"]\n");
                Messages.showMessageDialog(project, _message.toString(), "DeployRes", Messages.getInformationIcon());
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(cfgFile)));
            String targetBasePath = br.readLine();
            br.close();
            if (targetBasePath == null || "".equals(targetBasePath.trim())) {
                _message.append(_path + "   -cfg invalid ["+CFG_NAME+"]\n");
                Messages.showMessageDialog(project, _message.toString(), "DeployRes", Messages.getInformationIcon());
                return;
            }
            File desc = new File(targetBasePath + File.separator + focusFile.getPath().replace(cfgFile.getParent(), ""));
            File src = focusFile.getPath().equals(cfgFile.getPath()) ? focusFile.getParentFile() : focusFile;
            File des = focusFile.getPath().equals(cfgFile.getPath()) ? desc.getParentFile() : desc;
            copy(src, des);
            _message.append(_path + "   -done.\n");
            Messages.showMessageDialog(project, _message.toString(), "DeployRes", Messages.getInformationIcon());
        } catch (Exception e) {
            Messages.showMessageDialog(project, e.getMessage(), "DeployRes", Messages.getInformationIcon());
        }

    }
}
