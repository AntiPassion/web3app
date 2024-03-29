package com.xinbo.chainblock.service;

import com.xinbo.chainblock.entity.*;

import java.util.List;

/**
 * @author tony
 * @date 8/15/22 4:06 下午
 * @desc file desc
 */
public interface CommonService {


    boolean financeAccount(List<FinanceEntity> financeList, List<MemberFlowEntity> flowList, List<StatisticsEntity> statisticsList);

    boolean transfer(MemberEntity member, MemberFlowEntity flow);

}
