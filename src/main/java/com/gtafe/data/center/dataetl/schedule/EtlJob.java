package com.gtafe.data.center.dataetl.schedule;

import org.quartz.*;

@DisallowConcurrentExecution
public class EtlJob implements Job{

    int taskId;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        EtlSchedule.taskIdQueue.offer(taskId);
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

}
