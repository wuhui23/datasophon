package com.datasophon.api.master.handler.host;

import com.datasophon.api.utils.CommonUtils;
import com.datasophon.api.utils.JSchUtils;
import com.datasophon.common.Constants;
import com.datasophon.common.enums.InstallState;
import com.datasophon.common.model.HostInfo;
import com.jcraft.jsch.Session;

public class UploadWorkerHandler implements DispatcherWorkerHandler {

    @Override
    public boolean handle(Session session, HostInfo hostInfo) {
        boolean uploadFile = JSchUtils.uploadFile(
                session,
                Constants.INSTALL_PATH,
                Constants.MASTER_MANAGE_PACKAGE_PATH +
                        Constants.SLASH +
                        Constants.WORKER_PACKAGE_NAME);
        if(uploadFile){
            hostInfo.setMessage("分发成功，开始校验md5");
            hostInfo.setProgress(25);
        }else{
            hostInfo.setMessage("分发主机管理agent安装包失败");
            hostInfo.setErrMsg("dispatcher host agent to " + hostInfo.getHostname() + " failed");
            CommonUtils.updateInstallState(InstallState.FAILED, hostInfo);
        }
        return uploadFile;
    }
}
