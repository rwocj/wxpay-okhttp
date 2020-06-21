package com.github.rwocj.wx.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class ValidateResult {

    private final static ObjectMapper mapper = new ObjectMapper();

    @JsonProperty(index = 1)
    String object;
    @JsonProperty(index = 2)
    String field;
    @JsonProperty(index = 3)
    String message;
    @JsonProperty(index = 4)
    Object rejectedValue;

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
