package com.example.blds;

import com.alibaba.fastjson.JSON;
import com.example.blds.entity.HzUser;
import org.apache.commons.lang.StringUtils;

import java.util.*;

public class test {
    public static void main(String[] args) {
        List<String> s=new ArrayList<>();
        s.add("1");
        s.add("2");
        s.add("3");
        String sss=StringUtils.strip(s.toString(), "[]");
        System.out.println(StringUtils.strip(s.toString(), "{}"));
        System.out.println();
    }
}

