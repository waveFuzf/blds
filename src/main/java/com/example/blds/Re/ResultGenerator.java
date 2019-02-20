package com.example.blds.Re;

public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    //成功
    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCodeEnum.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return new Result()
                .setCode(ResultCodeEnum.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static <T> Result<T> genFailResult(T data) {
        return new Result()
                .setCode(ResultCodeEnum.FAIL)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setCode(ResultCodeEnum.FAIL)
                .setMessage(message);
    }

    public static Result genUnauthorizedResult() {
        return new Result()
                .setCode(ResultCodeEnum.UNAUTHORIZED)
                .setMessage("权限不足！");
    }

    public static <T> Result<T> genSuccessResult(T data,long total) {
        return new Result()
                .setCode(ResultCodeEnum.SUCCESS)
                .setMessage(String.valueOf(total))
                .setData(data);
    }
}