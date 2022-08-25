package com.xinbo.chainblock.consts;

/**
 * @author tony
 * @date 8/25/22 4:09 下午
 * @desc file desc
 */
public interface ActivityConst {

    int UNHANDLED_RECORD = 0;       //未处理
    int COMPLETE_RECORD = 1;        //完成
    int REJECT_RECORD = 2;          //拒绝


    int JUST_SEND_RULE = 1;         // 直接发放
    int ADMIN_CHECK_RULE = 2;       // 后端审核
    int AUTO_SEND = 3;              // 自动发放
}
