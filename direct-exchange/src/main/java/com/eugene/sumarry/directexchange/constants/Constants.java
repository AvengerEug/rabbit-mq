package com.eugene.sumarry.directexchange.constants;

public class Constants {

    public static final String DIRECT_EXCHANGE_NAME = "rabbit:mq03:direct-exchange:eug";

    public static final String SUCCESS_QUEUE = "rabbit:mq:03:queue:success";

    public static final String FAILED_QUEUE = "rabbit:mq:03:queue:failed";

    public static final String SUCCESS_ROUTING_KEY = "success";

    public static final String FAILED_ROUTING_KEY = "failed";

    public static final String ROUTING_KEY_SUCCESS_CONSUMER = "success_consumer";

    public static final String ROUTING_KEY_FAILED_CONSUMER = "failed_consumer";
}
