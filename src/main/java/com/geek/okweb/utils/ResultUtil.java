package com.geek.okweb.utils;

public class ResultUtil {

    public static Result success(Object object){
        Result result = new Result();

        result.setMessage("success");
        result.setCode(0);
        result.setData(object);

        return result;
    }

    public static Result fail(Object object){
        Result result = new Result();

        result.setMessage("error");
        result.setCode(1);
        result.setData(object);
        return result;
    }

    public static Result fail(){
        Result result = new Result();

        result.setMessage("error");
        result.setCode(1);
        return result;
    }

    public static Result verify(String message){
        Result result = new Result();

        result.setMessage(message);
        result.setCode(1);
        return result;
    }

    public static Result confirmPassword(){
        Result result = new Result();

        result.setMessage("两次密码不一致");
        result.setCode(2);
        return result;
    }

}
