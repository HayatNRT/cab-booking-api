package com.uber.uberapi.services.messagequeue;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DummyMessageMQImpl implements MQMessage {
    public String msg;
}
