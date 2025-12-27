package com.flintzy.socialtool.payload;

import lombok.Data;

@Data
public class PostRequest {
    private String pageId;
    private String message;
}
