package com.gtafe.data.center.system.user.vo;

import java.util.List;

/**
 * @Author: gang, zhou
 * @Date: 2017/12/5
 * @Description:
 */
public class MessageVo {
    int successCount;
    int errorCount;
    private List<ErrorVo> errorVos;
    private List<SysUserVo> sysUserVos;

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public List<ErrorVo> getErrorVos() {
        return errorVos;
    }

    public void setErrorVos(List<ErrorVo> errorVos) {
        this.errorVos = errorVos;
    }

    public List<SysUserVo> getSysUserVos() {
        return sysUserVos;
    }

    public void setSysUserVos(List<SysUserVo> sysUserVos) {
        this.sysUserVos = sysUserVos;
    }
}
