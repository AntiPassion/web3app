package com.xinbo.chainblock.service;
import com.xinbo.chainblock.bo.BasePageBo;
import com.xinbo.chainblock.entity.MemberEntity;
<<<<<<< HEAD
=======
import com.xinbo.chainblock.entity.admin.UserEntity;
import com.xinbo.chainblock.entity.terminal.BaseEntity;
import com.xinbo.chainblock.entity.terminal.TransactionApiEntity;
import com.xinbo.chainblock.utils.R;

import java.util.Date;
import java.util.Map;
>>>>>>> 30e5a312183241d17cdf3808671b354753f201c8

/**
 * @author tony
 * @date 6/24/22 4:31 下午
 * @desc file desc
 */
public interface MemberService {

    boolean insert();

    MemberEntity findByUsername(String username);

    MemberEntity findById(int id);

    boolean increment(MemberEntity entity);

    boolean register(MemberEntity entity, int code);

    BasePageBo findPage(MemberEntity entity, long current, long size);

    boolean update(MemberEntity entity);

<<<<<<< HEAD
    /**
     * 会员信息
     * @param uid
     * @return
     */
    MemberEntity info(int uid);
=======
    String balanceUSDT(int uid);

    Map<String,String> balance(int uid);


    /**
     * 资金帐户 => 交易帐户
     * @param uid
     * @param money
     * @return
     */
    BaseEntity<TransactionApiEntity> fundingAccount2TradingAccount(int uid, float money);

    /**
     * 交易帐户 => 资金帐户
     * @param uid
     * @param money
     * @return
     */
    BaseEntity<TransactionApiEntity> tradingAccount2FundingAccount(int uid, float money);
>>>>>>> 30e5a312183241d17cdf3808671b354753f201c8
}
