package com.xinbo.chainblock.jobs;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xinbo.chainblock.consts.RedisConst;
import com.xinbo.chainblock.core.TrxApi;
import com.xinbo.chainblock.entity.FinanceEntity;
import com.xinbo.chainblock.entity.WalletEntity;
import com.xinbo.chainblock.mapper.FinanceMapper;
import com.xinbo.chainblock.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author tony
 * @date 6/25/22 2:59 下午
 * @desc file desc
 */
@Component
public class FinanceRecordJob {

    @Autowired
    private TrxApi trxApi;

    @Autowired
    private CommonUtils commonUtils;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${trx.token-info.symbol}")
    private String tokenSymbol;

    @Value("${trx.token-info.name}")
    private String tokenName;

    @Autowired
    private FinanceMapper financeMapper;

    /**
     * 处理记录
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void handleRecord() {
        try {
            String json = redisTemplate.opsForSet().pop(RedisConst.MEMBER_FINANCE);
            if (StringUtils.isEmpty(json)) {
                return;
            }

            WalletEntity walletEntity = JSON.parseObject(json, WalletEntity.class);
            if (ObjectUtils.isEmpty(walletEntity) || walletEntity.getId() <= 0) {
                return;
            }

            List<FinanceEntity> financeEntityList = new ArrayList<>();
            String base58Address = walletEntity.getAddressBase58();
            String hexAddress = walletEntity.getAddressHex();
            /***************************** 处理Trc20记录  **********************************/
            JSONObject trc20Record = trxApi.getTrc20Record(base58Address);
            JSONArray trc20Data = trc20Record.getJSONArray("data");

            if (!ObjectUtils.isEmpty(trc20Data)) {
                for (int i = 0; i < trc20Data.size(); i++) {
                    JSONObject jsonObject = trc20Data.getJSONObject(i);
                    if (ObjectUtils.isEmpty(jsonObject)) {
                        continue;
                    }

                    String transactionId = jsonObject.getString("transaction_id");
                    long blockTimestamp = jsonObject.getLong("block_timestamp");
                    String fromAddress = jsonObject.getString("from");
                    String toAddress = jsonObject.getString("to");
                    BigInteger value = jsonObject.getBigInteger("value");

                    JSONObject tokenInfo = jsonObject.getJSONObject("token_info");
                    String symbol = tokenInfo.getString("symbol");
                    int decimals = tokenInfo.getInteger("decimals");
                    String name = tokenInfo.getString("name");


                    if (StringUtils.isEmpty(symbol) || !symbol.equals(tokenSymbol)) {
                        continue;
                    }

                    if (StringUtils.isEmpty(name) || !name.equals(tokenName)) {
                        continue;
                    }
                    BigDecimal b1 = new BigDecimal(value);
                    BigDecimal b2 = new BigDecimal(String.format("%s", Math.pow(10, decimals)));
                    BigDecimal b3 = b1.divide(b2, 2, RoundingMode.DOWN);
                    int type = base58Address.toUpperCase(Locale.ROOT).equals(toAddress.toUpperCase(Locale.ROOT)) ? 1 : 2;
                    FinanceEntity fe = FinanceEntity.builder()
                            .uid(walletEntity.getUid())
                            .username(walletEntity.getUsername())
                            .transactionId(transactionId)
                            .fromAddress(fromAddress)
                            .toAddress(toAddress)
                            .money(b3.floatValue())
                            .blockTime(DateUtil.date(blockTimestamp))
                            .blockTimestamp(blockTimestamp)
                            .symbol(symbol)
                            .type(type)
                            .isAccount(false)
                            .build();
                    financeEntityList.add(fe);
                }
            }

            /***************************** 处理Trx记录  **********************************/
            JSONObject trxRecord = trxApi.getTrxRecord(base58Address);
            JSONArray trxData = trxRecord.getJSONArray("data");
            if (!ObjectUtils.isEmpty(trxData)) {
                for (int i = 0; i < trxData.size(); i++) {
                    JSONObject jsonObject = trxData.getJSONObject(i);
                    if (ObjectUtils.isEmpty(jsonObject)) {
                        continue;
                    }

                    String txID = jsonObject.getString("txID");
                    if (StringUtils.isEmpty(txID)) {
                        continue;
                    }

                    JSONObject rawData = jsonObject.getObject("raw_data", JSONObject.class);
                    if (ObjectUtils.isEmpty(rawData)) {
                        continue;
                    }

                    long timestamp = rawData.getLong("timestamp");
                    JSONObject contract = rawData.getJSONArray("contract").getJSONObject(0);
                    JSONObject parameter = contract.getObject("parameter", JSONObject.class);
                    JSONObject value = parameter.getObject("value", JSONObject.class);
                    BigDecimal amount = value.getBigDecimal("amount");
                    String ownerAddress = value.getString("owner_address");
                    String toAddress = value.getString("to_address");


                    int type = toAddress.toUpperCase(Locale.ROOT).equals(hexAddress.toUpperCase(Locale.ROOT)) ? 1 : 2;
                    FinanceEntity fe = FinanceEntity.builder()
                            .uid(walletEntity.getUid())
                            .username(walletEntity.getUsername())
                            .transactionId(txID)
                            .fromAddress(ownerAddress)
                            .toAddress(toAddress)
                            .money(commonUtils.fromTrx(amount.floatValue()))
                            .blockTime(DateUtil.date(timestamp))
                            .blockTimestamp(timestamp)
                            .symbol("trx")
                            .type(type)
                            .isAccount(false)
                            .build();
                    financeEntityList.add(fe);
                }
            }

            /***************************** 保存数据库  **********************************/
            if (financeEntityList.size() > 0) {
                financeMapper.batchInsert(financeEntityList);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    /**
     * 处理入帐
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void handleAccount() {
        List<FinanceEntity> unaccounted = financeMapper.findUnaccounted();
        if(CollectionUtils.isEmpty(unaccounted)) {
            return;
        }

        /* **************************** 统计  ********************************* */

        /* **************************** 帐变  ********************************* */
    }
}
