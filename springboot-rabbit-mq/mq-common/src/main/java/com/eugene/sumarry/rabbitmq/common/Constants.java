package com.eugene.sumarry.rabbitmq.common;

public interface Constants {

    String ORDER_QUEUE_NAME = "orderService";

    String ORDER_CREATE_ROUTING_KEY = "order.create";

    String ORDER_SERVICE_MATCH_PREFIX_ROUTING_KEY = "order.#";

    String TOPIC_EXCHANGE = "topicExchange";

    String DEFAULT_EXCHANGE = "defaultExchange";

}
