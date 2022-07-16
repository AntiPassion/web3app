package com.xinbo.chainblock.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xinbo.chainblock.entity.HashOddsEntity;
import com.xinbo.chainblock.mapper.LotteryPlayCodeMapper;
import com.xinbo.chainblock.service.HashOddsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author tony
 * @date 6/24/22 4:31 下午
 * @desc file desc
 */
@Service
public class HashOddsServiceImpl extends ServiceImpl<LotteryPlayCodeMapper, HashOddsEntity> implements HashOddsService {

    @Autowired
    private LotteryPlayCodeMapper lotteryPlayCodeMapper;



    @Override
    public HashOddsEntity findById(int id) {
        return lotteryPlayCodeMapper.selectById(id);
    }


    /**
     * 创建查询条件
     *
     * @param entity  实体
     * @return LambdaQueryWrapper
     */
    private LambdaQueryWrapper<HashOddsEntity> createWrapper(HashOddsEntity entity) {
        LambdaQueryWrapper<HashOddsEntity> wrappers = Wrappers.lambdaQuery();
        if (ObjectUtils.isEmpty(entity)) {
            return wrappers;
        }
        if (!StringUtils.isEmpty(entity.getCode())) {
            wrappers.eq(HashOddsEntity::getCode, entity.getCode());
        }
        return wrappers;
    }
}
