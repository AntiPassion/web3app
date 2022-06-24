package com.xinbo.chainblock.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xinbo.chainblock.consts.TerminalApiConst;
import com.xinbo.chainblock.entity.terminal.ResponseEntity;
import com.xinbo.chainblock.entity.terminal.AccountApiEntity;
import com.xinbo.chainblock.entity.terminal.TransactionInfoApiEntity;
import com.xinbo.chainblock.entity.terminal.TransactionTrxApiEntity;
import com.xinbo.chainblock.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author tony
 * @date 6/23/22 5:38 下午
 * @desc file desc
 */
@Service
@Slf4j
public class TerminalApi {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommonUtils commonUtils;

    @Value("${terminal.url}")
    private String terminalUrl;

    /**
     * 创建帐号
     * @return
     */
    public AccountApiEntity createAccount() {
        AccountApiEntity result = null;
        try {
            String url = String.format("%s%s", terminalUrl, TerminalApiConst.CREATE_ACCOUNT);
            String res = restTemplate.postForObject(url, "", String.class);
            ResponseEntity<AccountApiEntity> entity = JSON.parseObject(res, new TypeReference<ResponseEntity<AccountApiEntity>>() {});
            if(!ObjectUtils.isEmpty(entity) && entity.getCode() == 0 && !ObjectUtils.isEmpty(entity.getData())) {
                result = entity.getData();
            }
        }catch (Exception ex) {
            log.error("TerminalApi createAccount exception", ex);
        }
        return result;
    }


    /**
     * 获取TRX余额
     * @param fromAddress 查询地址
     * @return String
     */
    public String getBalanceOfTrx(String fromAddress) {
        String result = null;
        try {
            String url = String.format("%s%s", terminalUrl, TerminalApiConst.GET_BALANCE_TRX);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fromAddress", fromAddress);
            String res = restTemplate.postForObject(url, jsonObject, String.class);
            ResponseEntity<String> entity = JSON.parseObject(res, new TypeReference<ResponseEntity<String>>() {});
            if(!ObjectUtils.isEmpty(entity) && entity.getCode() == 0) {
                result = entity.getData();
            }
        }catch (Exception ex) {
            log.error("TerminalApi getBalanceOfTrx exception", ex);
        }
        return result;
    }



    /**
     * 获取Trc20余额(USDT)
     * @param fromAddress 查询地址
     * @param privateKey  私钥
     * @return String
     */
    public String getBalanceOfTrc20(String fromAddress, String privateKey) {
        String result = null;
        try {
            String url = String.format("%s%s", terminalUrl, TerminalApiConst.GET_BALANCE_TRC20);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fromAddress", fromAddress);
            jsonObject.put("privateKey", privateKey);
            String res = restTemplate.postForObject(url, jsonObject, String.class);
            ResponseEntity<String> entity = JSON.parseObject(res, new TypeReference<ResponseEntity<String>>() {});
            if(!ObjectUtils.isEmpty(entity) && entity.getCode() == 0) {
                result = entity.getData();
            }
        }catch (Exception ex) {
            log.error("TerminalApi getBalanceOfTrc20 exception", ex);
        }
        return result;
    }



    /**
     * 获取Trc20余额(USDT)
     * @param fromAddress 转帐地址
     * @param privateKey  私钥
     * @param amount      数量
     * @param toAddress   收款地址
     * @return Entity
     */
    public TransactionTrxApiEntity transactionOfTrx(String fromAddress, String privateKey, double amount, String toAddress) {
        TransactionTrxApiEntity result = null;
        try {
            String url = String.format("%s%s", terminalUrl, TerminalApiConst.TRANSACTION_TRX);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fromAddress", fromAddress);
            jsonObject.put("amount", commonUtils.toTrx(amount));
            jsonObject.put("toAddress", toAddress);
            jsonObject.put("privateKey", privateKey);
            String res = restTemplate.postForObject(url, jsonObject, String.class);
            ResponseEntity<TransactionTrxApiEntity> entity = JSON.parseObject(res, new TypeReference<ResponseEntity<TransactionTrxApiEntity>>() {});
            if(!ObjectUtils.isEmpty(entity) && entity.getCode() == 0) {
                result = entity.getData();
            }
        }catch (Exception ex) {
            log.error("TerminalApi transactionOfTrx exception", ex);
        }
        return result;
    }




    /**
     * 获取Trc20余额(USDT)
     * @param contractAddress 合约地址
     * @param fromAddress 转帐地址
     * @param privateKey  私钥
     * @param amount      数量
     * @param toAddress   收款地址
     * @return String
     */
    public TransactionTrxApiEntity transactionOfTrc20(String contractAddress, String fromAddress, String privateKey, double amount, String toAddress) {
        TransactionTrxApiEntity result = null;
        try {
            String url = String.format("%s%s", terminalUrl, TerminalApiConst.TRANSACTION_TRC20);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("contractAddress", contractAddress);
            jsonObject.put("fromAddress", fromAddress);
            jsonObject.put("amount", commonUtils.toTrc20(amount));
            jsonObject.put("toAddress", toAddress);
            jsonObject.put("privateKey", privateKey);
            String res = restTemplate.postForObject(url, jsonObject, String.class);
            ResponseEntity<TransactionTrxApiEntity> entity = JSON.parseObject(res, new TypeReference<ResponseEntity<TransactionTrxApiEntity>>() {});
            if(!ObjectUtils.isEmpty(entity) && entity.getCode() == 0) {
                result = entity.getData();
            }
        }catch (Exception ex) {
            log.error("TerminalApi transactionOfTrc20 exception", ex);
        }
        return result;
    }


    /**
     * 获取交易信息
     * @param txID  转帐id
     * @return entity
     */
    public TransactionInfoApiEntity getTransactionInfo(String txID) {
        TransactionInfoApiEntity result = null;
        try {
            String url = String.format("%s%s", terminalUrl, TerminalApiConst.GET_TRANSACTION_INFO);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("txID", txID);
            String res = restTemplate.postForObject(url, jsonObject, String.class);
            ResponseEntity<TransactionInfoApiEntity> entity = JSON.parseObject(res, new TypeReference<ResponseEntity<TransactionInfoApiEntity>>() {});
            if(!ObjectUtils.isEmpty(entity) && entity.getCode() == 0) {
                result = entity.getData();
            }
        }catch (Exception ex) {
            log.error("TerminalApi getTransactionInfo exception", ex);
        }
        return result;
    }


}
