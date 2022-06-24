package com.xinbo.chainblock.jobs;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author tony
 * @date 6/23/22 4:48 下午
 * @desc file desc
 */
@Service
public class DrawNiuNiuJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void NuiNiu() {
        System.out.println("@Scheduled"+new Date());
    }


}