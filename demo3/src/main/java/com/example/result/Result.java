package com.example.result;

import lombok.Data;

@Data
public class Result<T> {
    private T data;
    private Boolean status;
    public static <T> Result<T> success(T data){
        Result<T> tResult = new Result<>();
        tResult.data=data;
        tResult.status=true;
        return tResult;
    }
    public static Result error(){
        Result result = new Result<>();
        result.status=false;
        return result;
    }
}
