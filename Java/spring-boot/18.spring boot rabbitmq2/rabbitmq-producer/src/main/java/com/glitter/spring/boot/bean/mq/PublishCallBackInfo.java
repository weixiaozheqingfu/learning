package com.glitter.spring.boot.bean.mq;

import lombok.Data;

@Data
public class PublishCallBackInfo {

    String correlationId;

    boolean returnedError;

    boolean ack;


}
