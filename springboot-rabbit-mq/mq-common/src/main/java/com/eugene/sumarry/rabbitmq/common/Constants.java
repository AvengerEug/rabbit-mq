package com.eugene.sumarry.rabbitmq.common;

public interface Constants {

    String ORDER_QUEUE_NAME = "orderService";

    String ORDER_CREATE_ROUTING_KEY = "order.create";

    String ORDER_SERVICE_MATCH_PREFIX_ROUTING_KEY = "order.#";

    String TOPIC_EXCHANGE = "topicExchange";

    String DEFAULT_EXCHANGE = "defaultExchange";

    // -------------------------------------------------

    String PRE_FETCH_EXCHANGE = "preFetchTopicExchange";

    String PRE_FETCH_QUEUE_NAME = "preFetchQueueName";

    String PRE_FETCH_ROUTING_KEY = "preFetchRoutingKey";


    // ----------------BASIC QUEUE
    String BASIC_QUEUE_EXCHANGE = "basicExchange";

    String BASIC_QUEUE = "basicQueue";

    String BASIC_QUEUE_ROUTING_KEY = "basicQueueRoutingKey.#";

    String BASIC_QUEUE_DEAD_LETTER_EXCHANGE = "basicQueueDeadLetterExchange";

    String BASIC_QUEUE_DEAD_LETTER_ROUTING_KEY = "basicQueueDeadLetterRoutingKey";

    String BASIC_QUEUE_DEAD_LETTER_QUEUE = "basicQueueDeadLetterQueue";
}
