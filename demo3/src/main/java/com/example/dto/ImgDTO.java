package com.example.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImgDTO {
    private MultipartFile img;
    private Integer id;
}
