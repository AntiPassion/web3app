package com.xinbo.chainblock.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tony
 * @date 6/25/22 4:10 下午
 * @desc file desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("t_statistics")
public class StatisticsEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    @TableField("date")
    private String date;

    /**
     * 用户id
     */
    @TableField("uid")
    private Integer uid;

    /**
     * 用户名
     */
    @TableField("username")
    private String username;

    /**
     * 投注总额
     */
    @TableField("bet_amount")
    private Float betAmount;

    /**
     * 当日投注次数
     */
    @TableField("bet_count")
    private Integer betCount;

    /**
     * 当日盈利总额
     */
    @TableField("profit_amount")
    private Float profitAmount;

    /**
     * 当日中奖总额
     */
    @TableField("payout_amount")
    private Float payoutAmount;

    /**
     * 充值trc20次数
     */
    @TableField("recharge_trc20_count")
    private Integer rechargeTrc20Count;

    /**
     * 充值trc20总额
     */
    @TableField("recharge_trc20_amount")
    private Float rechargeTrc20Amount;

    /**
     * 提现trc20总额
     */
    @TableField("withdraw_trc20_amount")
    private Float withdrawTrc20Amount;

    /**
     * 充值trx次数
     */
    @TableField("recharge_trx_count")
    private Integer rechargeTrxCount;

    /**
     * 充值trx总额
     */
    @TableField("recharge_trx_amount")
    private Float rechargeTrxAmount;

    /**
     * 提现trx总额
     */
    @TableField("withdraw_trx_amount")
    private Float withdrawTrxAmount;

    /**
     * 佣金总额
     */
    @TableField("commission_amount")
    private Float commissionAmount;

    /**
     * 活动总额
     */
    @TableField("activity_amount")
    private Float activityAmount;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

}
