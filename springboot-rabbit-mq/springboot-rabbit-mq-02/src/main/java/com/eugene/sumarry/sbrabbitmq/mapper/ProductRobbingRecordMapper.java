package com.eugene.sumarry.sbrabbitmq.mapper;

import com.eugene.sumarry.sbrabbitmq.entity.ProductRobbingRecord;

public interface ProductRobbingRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductRobbingRecord record);

    int insertSelective(ProductRobbingRecord record);

    ProductRobbingRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductRobbingRecord record);

    int updateByPrimaryKey(ProductRobbingRecord record);
}