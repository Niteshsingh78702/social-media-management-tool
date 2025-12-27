package com.flintzy.socialtool.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FacebookPagesResponse {
    
    private List<FacebookPage> data;
    
    @Data
    public static class FacebookPage {
        private String id;
        private String name;
        
        @JsonProperty("access_token")
        private String accessToken;
    }
}
