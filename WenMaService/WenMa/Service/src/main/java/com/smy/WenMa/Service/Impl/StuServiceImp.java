package com.smy.WenMa.Service.Impl;

import Entity.Stu;
import Mapper.StuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smy.WenMa.Service.StuService;
import com.smy.WenMa.Tool.Jwt;
import com.smy.WenMa.Tool.LoginInfo;
import com.smy.WenMa.Tool.LoginResult;
import com.smy.WenMa.Tool.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StuServiceImp implements StuService {
    @Autowired
    StuMapper stuMapper;
    @Autowired
    Jwt jwt;

    //    登录
    @Override
    public Result Login(String username, String password) {
        LambdaQueryWrapper<Stu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Stu::getStuId, username).eq(Stu::getPassword, password);
        Stu stu = stuMapper.selectOne(queryWrapper);
        if (stu == null) {
            return Result.error(500, "该用户不存在", null);
        } else {
            LoginResult loginResult = new LoginResult();
            loginResult.setUserId(stu.getStuId());
            loginResult.setName(stu.getName());
            loginResult.setUserType("1");
            //            生成令牌
            Map<String, String> map = new HashMap<>();
            map.put("userId", stu.getStuId());
            map.put("name", stu.getName());
            map.put("userType", "1");
            String token = jwt.generateToken(map);
            loginResult.setToken(token);
            return Result.success(200, "success", loginResult);
        }
    }


}
