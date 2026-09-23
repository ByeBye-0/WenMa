package com.smy.WenMa.Exception;


import com.smy.WenMa.Tool.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

//@RestControllerAdvice
public class SqlExpection extends Exception {
    @ExceptionHandler(Exception.class)
    public Result SQLException(Exception e) {
        return new Result(500,e.getMessage(),"未知错误");
    }

}
