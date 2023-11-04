package com.example.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class BreakPriceUrl implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String batch_no;
    private String platform_name;
    private String page_url;
    private String sku_id;
}
