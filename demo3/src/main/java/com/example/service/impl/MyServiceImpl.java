package com.example.service.impl;

import com.example.dao.MyDao;
import com.example.entity.BreakPriceUrl;
import com.example.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MyServiceImpl implements MyService {
    @Autowired
    private MyDao myDao;
    @Override
    public List<BreakPriceUrl> get(String platform, Integer count, String batchNo) {
        return myDao.get(platform,count,batchNo);
    }

    @Override
    public String uploadImg(MultipartFile img, Integer id) throws IOException {
        String url=myDao.search(id);
        if(url!=null) return null;
        // 原图位置, 输出图片位置, 水印文字颜色, 水印文字
        // 读取原图片信息
        Image srcImg = ImageIO.read(img.getInputStream());
        int srcImgWidth = srcImg.getWidth(null);
        int srcImgHeight = srcImg.getHeight(null);
        // 加水印
        BufferedImage bufImg = new BufferedImage(srcImgWidth,
                srcImgHeight,
                BufferedImage.TYPE_INT_RGB);
        //获取 Graphics2D 对象
        Graphics2D g = bufImg.createGraphics();
        //设置绘图区域
        g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
        //设置字体
        Font font = new Font("宋体", Font.PLAIN, 40);
        // 根据图片的背景设置水印颜色
        g.setColor(Color.green);
        g.setFont(font);

        LocalDateTime currentDateTime = LocalDateTime.now();

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 使用DateTimeFormatter类将LocalDateTime对象格式化为字符串
        String formattedDateTime = currentDateTime.format(formatter);


        //获取文字长度
        int len = g.getFontMetrics(
                g.getFont()).charsWidth(formattedDateTime.toCharArray(),
                0,
                formattedDateTime.length());
        int x = srcImgWidth - len - 10;
        int y = srcImgHeight - 20;
        g.drawString(formattedDateTime, x, y);
        g.dispose();
        // 输出图片
        String fileSrc = System.getProperty("user.dir")+"\\"+formattedDateTime +"\\"+id+".png";
        File waterFile = new File(formattedDateTime);
        if(!waterFile.exists()) waterFile.mkdirs();
        FileOutputStream outImgStream = new FileOutputStream(fileSrc);
        ImageIO.write(bufImg, "png", outImgStream);
        outImgStream.flush();
        outImgStream.close();
        myDao.insert(id,fileSrc);
        return fileSrc;
    }

}
