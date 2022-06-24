package com.xinbo.chainblock.controller;

import com.xinbo.chainblock.consts.StatusCode;
import com.xinbo.chainblock.core.TerminalApi;
import com.xinbo.chainblock.entity.terminal.AccountApiEntity;
import com.xinbo.chainblock.entity.terminal.TransactionInfoApiEntity;
import com.xinbo.chainblock.entity.terminal.TransactionTrxApiEntity;
import com.xinbo.chainblock.service.UserService;
import com.xinbo.chainblock.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tony
 * @date 6/23/22 5:48 下午
 * @desc file desc
 */
@RestController
public class ApiController {

    @Autowired
    private TerminalApi terminalApi;

    @Operation(summary = "createAccount")
    @GetMapping("createAccount")
    public R<Object> createAccount() {
        AccountApiEntity account = terminalApi.createAccount();
        return R.builder().code(StatusCode.SUCCESS).data(account).build();
    }

    @Operation(summary = "getBalanceOfTrx")
    @GetMapping("getBalanceOfTrx/{fromAddress}")
    public R<Object> getBalanceOfTrx(@PathVariable String fromAddress) {
        String balanceOfTrx = terminalApi.getBalanceOfTrx(fromAddress);
        return R.builder().code(StatusCode.SUCCESS).data(balanceOfTrx).build();
    }

    @Operation(summary = "getBalanceOfTrc20")
    @GetMapping("getBalanceOfTrc20/{fromAddress}/{privateKey}")
    public R<Object> getBalanceOfTrc20(@PathVariable String fromAddress, @PathVariable String privateKey) {
        String balanceOfTrx = terminalApi.getBalanceOfTrc20(fromAddress, privateKey);
        return R.builder().code(StatusCode.SUCCESS).data(balanceOfTrx).build();
    }

    @Operation(summary = "transactionOfTrx")
    @GetMapping("transactionOfTrx/{fromAddress}/{privateKey}/{amount}/{toAddress}")
    public R<Object> transactionOfTrx(@PathVariable String fromAddress, @PathVariable String privateKey, @PathVariable double amount, @PathVariable String toAddress) {
        TransactionTrxApiEntity entity = terminalApi.transactionOfTrx(fromAddress, privateKey, amount, toAddress);
        return R.builder().code(StatusCode.SUCCESS).data(entity).build();
    }


    @Operation(summary = "transactionOfTrc20")
    @GetMapping("transactionOfTrc20")
    public R<Object> transactionOfTrc20() {
        String contractAddress = "TZ5YTid3VphzLpgwSks24KFuyL7wgxuEBR";
        String fromAddress = "TDJJqGNpkZpSioBegZM8yyq1K7YnZA17nu";
        String toAddress = "TEuyVZdSXR8PaFmB8wX1LiZ3getos5Yuwe";
        String privateKey = "f58c1b3a3db8c4024d34427543dfcd6482b0bc7a0619a7d344b216a3be4f7703";
        double amount = 1.985;
        TransactionTrxApiEntity entity = terminalApi.transactionOfTrc20(contractAddress,fromAddress, privateKey, amount, toAddress);
        return R.builder().code(StatusCode.SUCCESS).data(entity).build();
    }




    @Operation(summary = "getTransactionInfo")
    @GetMapping("getTransactionInfo/{txID}")
    public R<Object> getTransactionInfo(@PathVariable String txID) {
        TransactionInfoApiEntity entity = terminalApi.getTransactionInfo(txID);
        return R.builder().code(StatusCode.SUCCESS).data(entity).build();
    }


}
