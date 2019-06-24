package com.geek.okweb.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -3911401494924149635L;

    private Integer code;

    private String message;

    private T data;

    public static Map<String,Object> toUrl(String url){
    	Map<String,Object> map=new HashMap<String,Object>();
    	map.put("tourl", url);
    	return map;
    }
    
}
