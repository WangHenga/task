package com.example.dao;

import com.example.entity.BreakPriceUrl;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MyDao {
    @Select("select * from haman_2023102801 where batch_no=#{batchNo} and platform_name=#{platform} limit #{count}")
    List<BreakPriceUrl> get(String platform, Integer count, String batchNo);
    @Select("select url from img_table where id=#{id};")
    String search(Integer id);
    @Insert("insert into img_table(id,url) value(#{id},#{fileSrc});")
    void insert(Integer id, String fileSrc);
}
