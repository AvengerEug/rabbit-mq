package com.eugene.sumarry.sbrabbitmq.listerner;

import com.eugene.sumarry.sbrabbitmq.Entity.OrderRecord;
import com.eugene.sumarry.sbrabbitmq.listerner.event.PushOrderRecordEvent;
import com.eugene.sumarry.sbrabbitmq.mapper.OrderRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

        /**
  * 这就是监听器跟RabbitMQ的Listener几乎是一个理念
  */
        @Component
public class PushOrderRecordListener implements ApplicationListener<PushOrderRecordEvent> {

    private static final Logger log = LoggerFactory.getLogger(PushOrderRecordListener.class);

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    @Override
    @Async
    public void onApplicationEvent(PushOrderRecordEvent event) {
        log.info("监听到的下单记录： {} ", event);

        try {
            Thread.sleep(10000);
            log.info("当前线程名: " + Thread.currentThread().getName());
            if (event != null) {
                OrderRecord entity = new OrderRecord();
                BeanUtils.copyProperties(event, entity);
                orderRecordMapper.insertSelective(entity);
            }
        } catch (Exception e) {
            log.error("监听下单记录发生异常：{} ", event, e.fillInStackTrace());
        }
    }
}
