package com.getbux.socket;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

class EventMessage {

    @JsonProperty("t")
    private String type;

    private String id;

    @JsonProperty("v")
    private Integer version;

    private Map body;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Map getBody() {
        return body;
    }

    public void setBody(Map body) {
        this.body = body;
    }
}