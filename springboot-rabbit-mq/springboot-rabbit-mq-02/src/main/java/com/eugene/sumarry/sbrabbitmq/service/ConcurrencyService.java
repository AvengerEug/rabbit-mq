package com.eugene.sumarry.sbrabbitmq.service;

import com.eugene.sumarry.sbrabbitmq.entity.Product;
import com.eugene.sumarry.sbrabbitmq.mapper.ProductMapper;
import com.eugene.sumarry.sbrabbitmq.mapper.ProductRobbingRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 高并发处理抢单服务
 */
@Service
public class ConcurrencyService {

    private static final Logger log = LoggerFactory.getLogger(ConcurrencyService.class);

    @Value("${product.id}")
    private String productNo;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductRobbingRecordMapper productRobbingRecordMapper;

    /**
     * 处理抢单
     * @param mobile
     */
    public void manageRobbing(String mobile){
        try {
            Product product = productMapper.selectByProductNo(productNo);
            if (product != null && product.getTotal() > 0) {
                log.info("当前手机号：{} 恭喜您抢到单了!", mobile);
                productMapper.updateTotal(product);
            } else {
                log.error("当前手机号：{} 抢不到单!", mobile);
            }
        } catch (Exception e) {
            log.error("处理抢单发生异常：mobile={} ", mobile);
        }
    }

}

















