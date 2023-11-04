package com.example.result;

import lombok.Data;

@Data
public class UrlResult {
    private String url;
    private Boolean status;
    public static UrlResult success(String url){
        UrlResult urlResult = new UrlResult();
        urlResult.url=url;
        urlResult.status=true;
        return urlResult;
    }
    public static UrlResult error(){
        UrlResult urlResult = new UrlResult();
        urlResult.status=false;
        return urlResult;
    }
}
