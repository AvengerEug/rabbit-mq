package com.eugene.sumarry.sbrabbitmq.mapper;


import com.eugene.sumarry.sbrabbitmq.entity.OrderRecord;

import java.util.List;

public interface OrderRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRecord record);

    int insertSelective(OrderRecord record);

    OrderRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRecord record);

    int updateByPrimaryKey(OrderRecord record);

    List<OrderRecord> selectAll();
}