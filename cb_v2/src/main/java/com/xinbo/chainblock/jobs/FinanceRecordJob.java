package com.xinbo.chainblock.jobs;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.xinbo.chainblock.consts.RedisConst;
import com.xinbo.chainblock.core.TrxApi;
import com.xinbo.chainblock.entity.FinanceEntity;
import com.xinbo.chainblock.entity.RechargeEntity;
import com.xinbo.chainblock.entity.MemberEntity;
import com.xinbo.chainblock.entity.WalletEntity;
import com.xinbo.chainblock.entity.terminal.TransactionRecordApiEntity;
import com.xinbo.chainblock.service.RechargeService;
import com.xinbo.chainblock.service.MemberService;
import com.xinbo.chainblock.service.WalletService;
import com.xinbo.chainblock.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tony
 * @date 6/25/22 2:59 下午
 * @desc file desc
 */
//@Component
public class FinanceRecordJob {

    @Autowired
    private TrxApi trxApi;

    @Autowired
    private CommonUtils commonUtils;


    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private MemberService memberService;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${trx.token-info.symbol}")
    private String tokenSymbol;

    @Value("${trx.token-info.name}")
    private String tokenName;


    private String transactionKey = "trx:transaction:%s";

    @Scheduled(cron = "0/5 * * * * ?")
    public void handleTrc20Rcord() {
        try {
            String json = redisTemplate.opsForSet().pop(RedisConst.MEMBER_FINANCE);
            if(StringUtils.isEmpty(json)) {
                return;
            }


            WalletEntity walletEntity = JSON.parseObject(json, WalletEntity.class);
            if (ObjectUtils.isEmpty(walletEntity) || walletEntity.getId() <= 0) {
                return;
            }

            String account = walletEntity.getAddressBase58();
            List<TransactionRecordApiEntity.Data> transactionsRecord = trxApi.getTransactionsRecord(account);
            if(ObjectUtils.isEmpty(transactionsRecord)) {
                return;
            }

            List<FinanceEntity> financeEntityList = new ArrayList<>();
            for (TransactionRecordApiEntity.Data data : transactionsRecord) {
                if(ObjectUtils.isEmpty(data)) {
                    continue;
                }

                if(ObjectUtils.isEmpty(data.getTokenInfo().getSymbol()) || !data.getTokenInfo().getSymbol().equals(tokenSymbol)) {
                    continue;
                }

                if(ObjectUtils.isEmpty(data.getTokenInfo().getName()) ||  !data.getTokenInfo().getName().equals(tokenName)) {
                    continue;
                }
                BigDecimal b1 = new BigDecimal(data.getValue());
                BigDecimal b2 = new BigDecimal(String.format("%s", Math.pow(10, data.getTokenInfo().getDecimals())));
                BigDecimal b3 = b1.divide(b2, 2, RoundingMode.DOWN);
                FinanceEntity fe = FinanceEntity.builder()
                        .uid(walletEntity.getUid())
                        .username(walletEntity.getUsername())
                        .transactionId(data.getTransactionId())
                        .fromAddress(data.getFrom())
                        .toAddress(data.getTo())
                        .money(b3.floatValue())
                        .blockTime(DateUtil.date(data.getBlockTimestamp()))
                        .blockTimestamp(data.getBlockTimestamp())
                        .symbol(data.getTokenInfo().getSymbol())
                        .type(1)
                        .isAccount(false)
                        .build();
                financeEntityList.add(fe);
            }
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }



}
