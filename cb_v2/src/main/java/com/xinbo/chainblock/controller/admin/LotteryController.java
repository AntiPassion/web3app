package com.xinbo.chainblock.controller.admin;

import com.xinbo.chainblock.annotation.JwtIgnore;
import com.xinbo.chainblock.consts.StatusCode;
import com.xinbo.chainblock.entity.*;
import com.xinbo.chainblock.entity.hash.HashPlayEntity;
import com.xinbo.chainblock.service.*;
import com.xinbo.chainblock.utils.R;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author tony
 * @date 7/11/22 7:24 下午
 * @desc file desc
 */
@RestController("adminLotteryController")
@RequestMapping("/admin/lottery")
public class LotteryController {

    @Autowired
    private GameService gameService;

    @Autowired
    private HashPlayService hashPlayService;

    @Autowired
    private HashOddsService hashOddsService;




    @JwtIgnore
    @Operation(summary = "findGame", description = "游戏列表")
    @PostMapping("findGame")
    public R<Object> findGame() {
        List<GameEntity> list = gameService.findAll();
        return R.builder().code(StatusCode.SUCCESS).data(list).build();
    }


    @JwtIgnore
    @Operation(summary = "findPlay", description = "游戏列表")
    @PostMapping("findPlay")
    public R<Object> findPlay() {
        List<HashPlayEntity> list = hashPlayService.findAll();
        return R.builder().code(StatusCode.SUCCESS).data(list).build();
    }

}
