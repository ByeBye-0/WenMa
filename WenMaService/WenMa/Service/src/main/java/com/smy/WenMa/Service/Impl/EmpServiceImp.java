package com.smy.WenMa.Service.Impl;

import Entity.Emp;
import Mapper.EmpMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smy.WenMa.Service.EmpService;
import com.smy.WenMa.Tool.Jwt;
import com.smy.WenMa.Tool.LoginResult;
import com.smy.WenMa.Tool.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmpServiceImp implements EmpService {
    @Autowired
    EmpMapper EmpMapper;
    @Autowired
    Jwt jwt;

    @Override
    public Result Login(String username, String password) {
        LambdaQueryWrapper<Emp> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Emp::getUsername, username).eq(Emp::getPassword, password);
        Emp emp = EmpMapper.selectOne(queryWrapper);
        if (emp == null) {
            return Result.error(500, "该员工不存在，请先注册", null);
        } else {
            LoginResult loginResult = new LoginResult();
            loginResult.setUserId(emp.getUsername());
            loginResult.setName(emp.getName());
            loginResult.setUserType("2");
            //            生成令牌
            Map<String, String> map = new HashMap<>();
            map.put("userId", emp.getUsername());
            map.put("name", emp.getName());
            map.put("userType","2");
            String token = jwt.generateToken(map);
            loginResult.setToken(token);
            return Result.success(200, "success", loginResult);
        }

    }
}
