package com.xinbo.chainblock.service;


import com.xinbo.chainblock.entity.AgentEntity;

import java.util.List;

/**
 * @author tony
 * @date 6/24/22 4:31 下午
 * @desc file desc
 */
public interface AgentService {

    boolean insert(AgentEntity entity);

    List<AgentEntity> findAll(int skip, int size);
}
