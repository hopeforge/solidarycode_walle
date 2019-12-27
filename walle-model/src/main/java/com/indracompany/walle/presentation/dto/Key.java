
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
    "remoteJid",
    "fromMe",
    "id"
})
public class Key {

    @JsonProperty("remoteJid")
    private String remoteJid;
    @JsonProperty("fromMe")
    private Boolean fromMe;
    @JsonProperty("id")
    private String id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("remoteJid")
    public String getRemoteJid() {
        return remoteJid;
    }

    @JsonProperty("remoteJid")
    public void setRemoteJid(String remoteJid) {
        this.remoteJid = remoteJid;
    }

    @JsonProperty("fromMe")
    public Boolean getFromMe() {
        return fromMe;
    }

    @JsonProperty("fromMe")
    public void setFromMe(Boolean fromMe) {
        this.fromMe = fromMe;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
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
