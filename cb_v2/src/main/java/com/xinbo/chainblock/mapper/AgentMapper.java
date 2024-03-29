package com.xinbo.chainblock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinbo.chainblock.entity.AgentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author tony
 * @date 6/24/22 4:24 下午
 * @desc file desc
 */
@Mapper
public interface AgentMapper extends BaseMapper<AgentEntity> {
    @Select("select * from t_agent where uid = #{code} limit 1")
    AgentEntity findByUid(@Param("code") int code);

    @Select("select * from t_agent order by id asc limit #{skip}, #{size}")
    List<AgentEntity> findAll(@Param("skip") int skip, @Param("size") int size);

    @Update("update t_agent set child = #{childStr} where id = #{id}")
    int setChild(@Param("id") int id, @Param("childStr") String childStr);

    @Select("select uid from t_agent where p_uid = #{uid}")
    List<AgentEntity> directly(@Param("uid") int uid);

    @Select("select * from t_agent where username = #{username} limit 1")
    AgentEntity findByUsername(@Param("username") String username);
}
