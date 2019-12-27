
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
    "Id",
    "RemoteJid",
    "SenderJid",
    "FromMe",
    "Timestamp",
    "PushName",
    "Status",
    "QuotedMessageID",
    "Source"
})
public class Info {

    @JsonProperty("Id")
    private String id;
    @JsonProperty("RemoteJid")
    private String remoteJid;
    @JsonProperty("SenderJid")
    private String senderJid;
    @JsonProperty("FromMe")
    private Boolean fromMe;
    @JsonProperty("Timestamp")
    private Integer timestamp;
    @JsonProperty("PushName")
    private String pushName;
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("QuotedMessageID")
    private String quotedMessageID;
    @JsonProperty("Source")
    private Source source;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Id")
    public String getId() {
        return id;
    }

    @JsonProperty("Id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("RemoteJid")
    public String getRemoteJid() {
        return remoteJid;
    }

    @JsonProperty("RemoteJid")
    public void setRemoteJid(String remoteJid) {
        this.remoteJid = remoteJid;
    }

    @JsonProperty("SenderJid")
    public String getSenderJid() {
        return senderJid;
    }

    @JsonProperty("SenderJid")
    public void setSenderJid(String senderJid) {
        this.senderJid = senderJid;
    }

    @JsonProperty("FromMe")
    public Boolean getFromMe() {
        return fromMe;
    }

    @JsonProperty("FromMe")
    public void setFromMe(Boolean fromMe) {
        this.fromMe = fromMe;
    }

    @JsonProperty("Timestamp")
    public Integer getTimestamp() {
        return timestamp;
    }

    @JsonProperty("Timestamp")
    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("PushName")
    public String getPushName() {
        return pushName;
    }

    @JsonProperty("PushName")
    public void setPushName(String pushName) {
        this.pushName = pushName;
    }

    @JsonProperty("Status")
    public Integer getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonProperty("QuotedMessageID")
    public String getQuotedMessageID() {
        return quotedMessageID;
    }

    @JsonProperty("QuotedMessageID")
    public void setQuotedMessageID(String quotedMessageID) {
        this.quotedMessageID = quotedMessageID;
    }

    @JsonProperty("Source")
    public Source getSource() {
        return source;
    }

    @JsonProperty("Source")
    public void setSource(Source source) {
        this.source = source;
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
