package com.gtafe.framework.base.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReadFileUtil {
    public ReadFileUtil() {
    }

    public static List<File> getFileList(String strPath, String parten) {
        List<File> filelist = new ArrayList<File>();
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath(), parten); // 获取文件绝对路径
                } else if (fileName.endsWith(parten)) { // 判断文件名是否以.avi结尾
                    String strFileName = files[i].getAbsolutePath();
                    filelist.add(files[i]);
                } else {
                    continue;
                }
            }
        }
        return filelist;
    }

    public static void main(String[] args) {
      //  getFileList("D:\\vjpageProject", "ktr");
       // System.out.println("ok");
    }

}
