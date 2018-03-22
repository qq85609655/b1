package com.gtafe.data.center.dataetl.datatask.vo;

import com.gtafe.data.center.common.common.vo.PageParam;

public class KfileParam extends PageParam {
    private String fileName;//文件名称
    private String fileType;//1 ktr  2 kjb

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
