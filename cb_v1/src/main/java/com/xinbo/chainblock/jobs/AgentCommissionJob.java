package com.xinbo.chainblock.jobs;

import cn.hutool.core.date.DateUtil;
import com.xinbo.chainblock.entity.AgentEntity;
import com.xinbo.chainblock.entity.AgentRebateEntity;
import com.xinbo.chainblock.entity.StatisticsEntity;
import com.xinbo.chainblock.mapper.AgentRebateMapper;
import com.xinbo.chainblock.service.AgentRebateService;
import com.xinbo.chainblock.service.AgentService;
import com.xinbo.chainblock.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author tony
 * @date 7/5/22 5:55 下午
 * @desc 计算代理佣金
 */
@Slf4j
@Component
public class AgentCommissionJob {

    @Autowired
    private AgentService agentService;

    @Autowired
    private AgentRebateService agentRebateService;

    @Autowired
    private StatisticsService statisticsService;


    @Scheduled(cron = "0/5 * * * * ?")
    public void handle() {


        List<AgentRebateEntity> rebates = agentRebateService.findAll();


        String date = "20220704";

        //Step 1: 获取代理层级表
        int page = 1;
        int size = 50;
        List<AgentEntity> list = new ArrayList<>();
        while (true) {
            int skip = (page - 1) * size;
            List<AgentEntity> res = agentService.findAll(skip, size);
            if (CollectionUtils.isEmpty(res) || res.size() <= 0) {
                break;
            }

            list = Stream.of(list, res).flatMap(Collection::stream).collect(Collectors.toList());

            if (res.size() != size) {
                break;
            }

            page += 1;
        }

        if(CollectionUtils.isEmpty(list)) {
            return;
        }

        Map<Integer, Double> map = new HashMap<>();

        List<AgentEntity> agentList = list.stream().filter(f -> !StringUtils.isEmpty(f.getChild())).collect(Collectors.toList());
        for(AgentEntity entity : agentList) {
            //下级
            List<Integer> childList = Arrays.stream(entity.getChild().split(",")).map(Integer::parseInt).collect(Collectors.toList());
            childList.add(entity.getUid());
            List<StatisticsEntity> childStatistics = statisticsService.findByUidStr(date, childList);

            double sum = childStatistics.stream().mapToDouble(StatisticsEntity::getBetMoney).sum();
            map.put(entity.getUid(),sum);
        }
        System.out.println(map);

        //汇总从下向上
        Map<Integer, Double> newMap = new HashMap<>();
        List<Integer> keys = map.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for(Integer uid : keys) {
            Double betMoney = map.get(uid);

            AgentEntity curAgentEntity = list.stream().filter(f -> f.getUid().equals(uid)).findFirst().orElse(null);
            if(ObjectUtils.isEmpty(curAgentEntity)) {
                continue;
            }
            AgentRebateEntity rebateEntity = this.getRebate(rebates, betMoney);
            Integer curRebate = rebateEntity.getRebate();


            int pUid = curAgentEntity.getPUid();
            AgentEntity parAgentEntity = list.stream().filter(f -> f.getUid().equals(pUid)).findFirst().orElse(null);
            if(ObjectUtils.isEmpty(parAgentEntity) || parAgentEntity.getPUid() == 0) {
                continue;
            }

            Double parBetMoney = map.get(parAgentEntity.getUid());
            rebateEntity = this.getRebate(rebates, parBetMoney);
            Integer parRebate = rebateEntity.getRebate();

            double cha = parRebate - curRebate;
            double tmp = cha * betMoney / 10000;

            Double r = newMap.containsKey(pUid) ? betMoney + newMap.get(pUid) : betMoney;
            newMap.put(pUid, r);
        }

        System.out.println(newMap);

        /*
//        String date = DateUtil.format(new Date(), "yyyyMMdd");

        Map<Integer, Float> map = new HashMap<>();

        //递归获取数据
        int i = 1;
        while (true) {
            int finalI = i;
            List<AgentEntity> agentList = list.stream().filter(f -> f.getLevel() == finalI).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(agentList) || agentList.size() <= 0) {
                break;
            }
            for (AgentEntity entity : agentList) {
                String child = entity.getChild();
                if(StringUtils.isEmpty(child)) {
                    continue;
                }

                List<Integer> childList = Arrays.stream(child.split(",")).map(Integer::parseInt).collect(Collectors.toList());
                for (int childUid : childList) {
                    StatisticsEntity childStatistics = statisticsService.findByUid(date, childUid);
                    map.put(childUid, childStatistics.getBetMoney());
                    System.out.println(childUid);
                }
                StatisticsEntity selfStatistics = statisticsService.findByUid(date, entity.getUid());
                map.put(entity.getUid(), selfStatistics.getBetMoney());
                System.out.println("----------------");
            }
            i += 1;
        }


        //汇总从下向上
        Map<Integer, Float> newMap = new HashMap<>();
        List<Integer> keys = map.keySet().stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for(Integer uid : keys) {
            Float betMoney = map.get(uid);

            AgentEntity curAgentEntity = list.stream().filter(f -> f.getUid().equals(uid)).findFirst().orElse(null);
            if(ObjectUtils.isEmpty(curAgentEntity)) {
                continue;
            }
            int pUid = curAgentEntity.getPUid();
            AgentEntity parAgentEntity = list.stream().filter(f -> f.getUid().equals(pUid)).findFirst().orElse(null);
            if(ObjectUtils.isEmpty(parAgentEntity) || parAgentEntity.getPUid() == 0) {
                Float r = newMap.containsKey(uid) ? betMoney + newMap.get(uid) : betMoney;
                newMap.put(uid, r);
                continue;
            }
            Float r = newMap.containsKey(pUid) ? betMoney + newMap.get(pUid) : betMoney;
            newMap.put(pUid, r);
        }


        System.out.println(newMap);*/


    }


    private AgentRebateEntity getRebate(List<AgentRebateEntity> list, double value) {
        for(AgentRebateEntity entity : list) {
            if(value > entity.getMin() && value <= entity.getMax()) {
                return entity;
            }
        }
        return null;
    }



    private List<AgentEntity> loop(List<AgentEntity> list, int uid) {
        //下级
        List<AgentEntity> childAgent = list.stream().filter(f -> f.getPUid() == uid).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(childAgent) || childAgent.size() <= 0) {
            //没有下级，返回当前节点
            return list.stream().filter(f -> f.getUid() == uid).collect(Collectors.toList());
        }
        List<AgentEntity> result = new ArrayList<>();
        for (AgentEntity child : childAgent) {
            List<AgentEntity> loop = this.loop(list, child.getUid());
            if (CollectionUtils.isEmpty(loop) || loop.size() <= 0) {
                return null;
            }

            result = Stream.of(result, loop).flatMap(Collection::stream).collect(Collectors.toList());
        }
        //递归完成，把上级节点合并进结果集
        result.addAll(list.stream().filter(f -> f.getUid() == uid).collect(Collectors.toList()));
        return result;
    }


}
