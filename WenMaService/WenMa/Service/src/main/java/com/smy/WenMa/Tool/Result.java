package com.smy.WenMa.Tool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private Integer code;
    private String msg;
    private Object data;

    public static Result success(Integer code, String msg, Object data) {
        Result res = new Result();
        res.code = code;
        res.msg = msg;
        res.data = data;
        return res;
    }
    public static Result error(Integer code, String msg, Object data) {
        Result res = new Result();
        res.code = code;
        res.msg = msg;
        res.data = data;
        return res;
    }
}
