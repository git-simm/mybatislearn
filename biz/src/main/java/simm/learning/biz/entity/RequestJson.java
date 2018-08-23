package simm.learning.biz.entity;
import com.fasterxml.jackson.annotation.*;

import javax.validation.Valid;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userId",
        "systemId",
        "time",
        "channel",
        "signature",
        "data"
})
public class RequestJson<T> {
    @JsonProperty("userId")
    private Integer userId;
    @JsonProperty("systemId")
    private String systemId;
    @JsonProperty("time")
    private String time;
    @JsonProperty("signature")
    private String signature;
    @JsonProperty("data")
    @Valid
    private T data;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    //@JsonProperty("data")
    public T getData() {
        return data;
    }

    //@JsonProperty("data")
    public void setData(T data) {
        this.data = data;
    }
}