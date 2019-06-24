package com.geek.okweb.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class VerifyUtil {

    public static String getRandomValue(){
        String code = "";
        int codeLength = 4;//验证码的长度
        List<Character> selectChar = Arrays.asList('1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');
        for(int i=0;i<codeLength;i++) {
            int charIndex = (int) Math.floor(Math.random()*56);
            code += selectChar.get(charIndex);
        }
        if(code.length() != codeLength){
            getRandomValue();
        }
        return code;
    }

}
