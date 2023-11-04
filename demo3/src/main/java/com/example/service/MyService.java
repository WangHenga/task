package com.example.service;

import com.example.entity.BreakPriceUrl;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MyService {

    List<BreakPriceUrl> get(String platform, Integer count, String batchNo);


    String uploadImg(MultipartFile img, Integer id) throws IOException;
}
