package com.datasophon.api.master.handler.host;

import com.datasophon.api.utils.JSchUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.model.HostInfo;
import com.jcraft.jsch.Session;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstallJDKHandler implements DispatcherWorkerHandler {

    private static final Logger logger = LoggerFactory.getLogger(InstallJDKHandler.class);

    @Override
    public boolean handle(Session session, HostInfo hostInfo) {
        hostInfo.setProgress(60);
        String arch = JSchUtils.execCmdWithResult(session, "arch");
        String testResult = JSchUtils.execCmdWithResult(session, "test -d /usr/local/jdk1.8.0_333");
        boolean exists = true;
        if(StringUtils.isNotBlank(testResult) && "failed".equals(testResult)){
            exists = false;
        }
        if("x86_64".equals(arch) ){
            if(!exists){
                hostInfo.setMessage("开始安装jdk");
                JSchUtils.uploadFile(session, "/usr/local", Constants.MASTER_MANAGE_PACKAGE_PATH + Constants.SLASH +Constants.X86JDK);
                JSchUtils.execCmdWithResult(session, "tar -zxvf /usr/local/jdk-8u333-linux-x64.tar.gz -C /usr/local/");
            }
        }
        if("aarch64".equals(arch)){
            if(!exists){
                hostInfo.setMessage("开始安装jdk");
                JSchUtils.uploadFile(session, "/usr/local",Constants.MASTER_MANAGE_PACKAGE_PATH + Constants.SLASH +Constants.ARMJDK);
                JSchUtils.execCmdWithResult(session, "tar -zxvf /usr/local/jdk-8u333-linux-aarch64.tar.gz -C /usr/local/");
            }
        }
        return true;
    }
}
