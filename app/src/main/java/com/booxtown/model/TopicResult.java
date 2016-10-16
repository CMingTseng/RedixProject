package com.booxtown.model;

import java.util.List;

/**
 * Created by duong on 9/16/2016.
 */
public class TopicResult {
    int code;
    public List<Topic> topic;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Topic> getTopic() {
        return topic;
    }

    public void setTopic(List<Topic> topic) {
        this.topic = topic;
    }

    public TopicResult(int code, List<Topic> topic) {
        this.code = code;
        this.topic = topic;
    }
}
