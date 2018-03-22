package com.gtafe.framework.base.utils;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

public class KettleExecuUtil {
    public KettleExecuUtil(){

    }

    public static int runJob(String jobname) {
        try {
            KettleEnvironment.init();
            // jobname 是Job脚本的路径及名称
            JobMeta jobMeta = new JobMeta(jobname, null);
            Job job = new Job(null, jobMeta);
            // 向Job 脚本传递参数，脚本中获取参数值：${参数名}
            // job.setVariable(paraname, paravalue);
            job.start();
            job.waitUntilFinished();
            if (job.getErrors() > 0) {
                System.out.println("decompress fail!");
            }
            return job.getErrors();
        } catch (KettleException e) {
            System.out.println(e);
        }
        return 1;
    }

    /**
     * 调用kettle 执行ktr文件
     *
     * @param filename
     * @return
     */
    public static int runTrans(String filename) {
        try {
            KettleEnvironment.init();
            TransMeta transMeta = new TransMeta(filename);
            Trans trans = new Trans(transMeta);
            trans.prepareExecution(null);
            trans.startThreads();
            trans.waitUntilFinished();
            if (trans.getErrors() != 0) {
                System.out.println("Error");
            }
            return trans.getErrors();
        } catch (KettleXMLException e) {
            e.printStackTrace();
        } catch (KettleException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static void main(String[] args) {
        // String jobname = "D:\\kettle-file.ktr";
        // runJob(jobname);
        String filename = "D:\\kettle-file.ktr";
        System.out.println(runTrans(filename));
    }
}
