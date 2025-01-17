package com.datasophon.api.master.handler.host;

import cn.hutool.core.io.FileUtil;
import com.datasophon.api.master.DispatcherWorkerActor;
import com.datasophon.api.utils.CommonUtils;
import com.datasophon.api.utils.JSchUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.enums.InstallState;
import com.datasophon.common.model.HostInfo;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class CheckWorkerMd5Handler implements DispatcherWorkerHandler{
    private static final Logger logger = LoggerFactory.getLogger(CheckWorkerMd5Handler.class);
    @Override
    public boolean handle(Session session, HostInfo hostInfo) {
        String checkWorkerMd5Result = JSchUtils.execCmdWithResult(session, Constants.CHECK_WORKER_MD5_CMD).trim();
        String md5 = FileUtil.readString(
                Constants.MASTER_MANAGE_PACKAGE_PATH +
                        Constants.SLASH +
                        Constants.WORKER_PACKAGE_NAME + ".md5",
                Charset.defaultCharset()).trim();
        logger.info("{} worker package md5 value is : {}",hostInfo.getHostname(),md5);
        if(!md5.equals(checkWorkerMd5Result)){
            logger.error("worker package md5 check failed");
            hostInfo.setErrMsg("worker package md5 check failed");
            hostInfo.setMessage("md5校验失败");
            CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
            return false;
        }
        hostInfo.setProgress(35);
        hostInfo.setMessage("md5校验成功，开始解压安装包");
        return true;
    }
}
