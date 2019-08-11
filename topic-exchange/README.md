# topic交换机

## 原理
```
    添加'.' 和 '#'通配符。
    在官网中的描述是: 
      通配符'.' 代表的是一个词(one word)  
      通配符'#' 代表的是0或者多个词(zero or more words)
      
    具体如下: 若定义了一个topic交换机但定义了两个队列,并且routingkey和队列的绑定关系如下:
      队列一: *.orange.*
      队列二: *.*.rabbit
      队列二: lazy.#
      
    那么在绑定队列指定key时,
    quick.orange.rabbit 将映射到队列一和队列二
    quick.orange.fox 只映射到队列一
```

## 注意点
1. 当一个队列有多个routingKey的通配项并且生产者发送消息时同时match上了这些通配项, 队列也只能收到一条消息