package com.example.controller;

import com.example.dto.ImgDTO;
import com.example.entity.BreakPriceUrl;
import com.example.result.Result;
import com.example.result.UrlResult;
import com.example.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class MyController {
    @Autowired
    private MyService myService;
    @GetMapping("/breakPriceUrls")
    public Result<List<BreakPriceUrl>> get(String platform,
                                           Integer count,
                                           String batchNo){
        if(count==null) count=10;
        if(platform==null||batchNo==null) return Result.error();
        List<BreakPriceUrl> res=myService.get(platform,count,batchNo);
        return Result.success(res);
    }
    @PostMapping("/uploadImg")
    public UrlResult upload(@RequestParam MultipartFile img,Integer id) throws IOException {
        if(img==null&&id==null) return UrlResult.error();
        String url=myService.uploadImg(img,id);
        if(url!=null){
            return UrlResult.success(url);
        }else{
            return UrlResult.error();
        }
    }
}
