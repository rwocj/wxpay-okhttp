package top.rwocj.wx.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidateResult {

    private final static ObjectMapper mapper = new ObjectMapper();

    @JsonProperty(index = 1)
    private final String object;
    @JsonProperty(index = 2)
    private final String field;
    @JsonProperty(index = 3)
    private final String message;
    @JsonProperty(index = 4)
    private final Object rejectedValue;

    public ValidateResult(String object, String field, String message, Object rejectedValue) {
        this.object = object;
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
