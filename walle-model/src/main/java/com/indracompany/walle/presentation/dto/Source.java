
package com.indracompany.walle.presentation.dto;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "key",
    "message",
    "messageTimestamp",
    "status",
    "ignore"
})
public class Source {

    @JsonProperty("key")
    private Key key;
    @JsonProperty("message")
    private Message message;
    @JsonProperty("messageTimestamp")
    private Integer messageTimestamp;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("ignore")
    private Boolean ignore;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("key")
    public Key getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(Key key) {
        this.key = key;
    }

    @JsonProperty("message")
    public Message getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(Message message) {
        this.message = message;
    }

    @JsonProperty("messageTimestamp")
    public Integer getMessageTimestamp() {
        return messageTimestamp;
    }

    @JsonProperty("messageTimestamp")
    public void setMessageTimestamp(Integer messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    @JsonProperty("status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("ignore")
    public Boolean getIgnore() {
        return ignore;
    }

    @JsonProperty("ignore")
    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
