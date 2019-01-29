package me.zhaotb.app.api;

public class Message {

    private String topic;
    private String key;
    private String body;

    public Message(String topic) {
        this.topic = topic;
    }

    public Message(String topic, String key) {
        this.topic = topic;
        this.key = key;
    }

    public Message(String topic, String key, String body) {
        this.topic = topic;
        this.key = key;
        this.body = body;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
