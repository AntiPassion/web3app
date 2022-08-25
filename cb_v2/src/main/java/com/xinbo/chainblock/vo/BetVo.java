package com.xinbo.chainblock.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tony
 * @date 6/30/22 6:27 下午
 * @desc file desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetVo {

    private int id;

    private int playId;

    private int gameId;

    private int type;

}
