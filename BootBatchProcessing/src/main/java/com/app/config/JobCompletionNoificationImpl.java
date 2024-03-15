package com.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNoificationImpl implements JobExecutionListener {

    Logger log = LoggerFactory.getLogger(JobCompletionNoificationImpl.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job Started!!");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus()== BatchStatus.COMPLETED)
        {
            log.info("Job Completed!!");
        }
    }
}
